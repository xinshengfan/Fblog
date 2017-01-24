package com.fblog.core.plugin;

import com.fblog.core.dao.entity.PageModel;
import com.fblog.core.utils.BeanPropertyUtils;
import com.fblog.core.utils.JdbcUtils;
import com.fblog.core.utils.LogUtils;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by fansion on 18/01/2017.
 * Mybatis的分页查询插件，通过拦截StatementHandler的prepare方法来实现。
 * 只有在参数列表中包括PageModel类型的参数时才进行分页查询。 在多参数的情况下，只对第一个PageModel类型的参数生效。
 * <p>
 * 利用拦截器实现Mybatis分页的原理：
 * 要利用JDBC对数据库进行操作就必须要有一个对应的Statement对象，Mybatis在执行Sql语句前就会产生一个包含Sql语句的Statement对象，而且对应的Sql语句
 * 是在Statement之前产生的，所以我们就可以在它生成Statement之前对用来生成Statement的Sql语句下手。在Mybatis中Statement语句是通过RoutingStatementHandler对象的
 * prepare方法生成的。所以利用拦截器实现Mybatis分页的一个思路就是拦截StatementHandler接口的prepare方法，然后在拦截器方法中把Sql语句改成对应的分页查询Sql语句，
 * 之后再调用 * StatementHandler对象的prepare方法，即调用invocation.proceed()。
 * 对于分页而言，在拦截器里面我们还需要做的一个操作就是统计满足当前条件的记录一共有多少，这是通过获取到了原始的Sql语句后，把它改为对应的统计语句再利用Mybatis
 * 封装好的参数和设 * 置参数的功能把Sql语句中的参数进行替换，之后再执行查询记录数的Sql语句进行总记录数的统计。
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PageInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //对于StatementHandler其实只有两个实现类，一个是RoutingStatementHandler，另一个是抽象类BaseStatementHandler，
        //BaseStatementHandler有三个子类，分别是SimpleStatementHandler，PreparedStatementHandler和CallableStatementHandler，
        //SimpleStatementHandler是用于处理Statement的，PreparedStatementHandler是处理PreparedStatement的，而CallableStatementHandler是
        //处理CallableStatement的。Mybatis在进行Sql语句处理的时候都是建立的RoutingStatementHandler，而在RoutingStatementHandler里面拥有一个
        //StatementHandler类型的delegate属性，RoutingStatementHandler会依据Statement的不同建立对应的BaseStatementHandler，即SimpleStatementHandler、
        //PreparedStatementHandler或CallableStatementHandler，在RoutingStatementHandler里面所有StatementHandler接口方法的实现都是调用的delegate对应的方法。
        //我们在PageInterceptor类上已经用@Signature标记了该Interceptor只拦截StatementHandler接口的prepare方法，又因为Mybatis只有在建立RoutingStatementHandler的时候
        //是通过Interceptor的plugin方法进行包裹的，所以我们这里拦截到的目标对象肯定是R
        RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
        //通过反射获取到当前RoutingStatementHandler对象的delegate属性
        BaseStatementHandler delegate = (BaseStatementHandler) BeanPropertyUtils.getFieldValue(handler, "delegate");
        //获取到当前StatementHandler的 boundSql，这里不管是调用handler.getBoundSql()还是直接调用delegate.getBoundSql()结果是一样的，因为之前已经说过了
        //RoutingStatementHandler实现的所有StatementHandler接口方法里面都是调用的
        BoundSql boundSql = delegate.getBoundSql();
        //拿到当前绑定Sql的参数对象，就是我们在调用对应的Mapper映射语句时所传入的参数
        Object param = boundSql.getParameterObject();
        if (param == null || !(param instanceof PageModel)) {
            return invocation.proceed();
        }
        //判断传入的是Page对象就认定它是需要进行分页操作的。
        PageModel<?> model = (PageModel<?>) param;
        if (model.isQueryAll()) {
            //查询全部，不用分页
            return invocation.proceed();
        }
        //获取当前要执行的Sql语句，也就是我们直接在Mapper映射语句中写的Sql语句
        String sql = boundSql.getSql();
        LogUtils.i("fan","执行的sql："+sql);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //拦截到的prepare方法参数是一个Connection对象
            Connection connection = (Connection) invocation.getArgs()[0];
            //查询总记录，
            String countSql = model.countSql(sql);
            ps = connection.prepareStatement(countSql);
            //利用反射设置当前BoundSql对应的sql属性为我们建立好的分页Sql语句
            BeanPropertyUtils.setFieldValue(boundSql, "sql", countSql);
            MappedStatement ms = (MappedStatement) BeanPropertyUtils.getFieldValue(delegate, "mappedStatement");
            DefaultParameterHandler dph = new DefaultParameterHandler(ms, param, boundSql);
            dph.setParameters(ps);
            rs = ps.executeQuery();
            long count = 0;
            if (rs.next()) {
                count = ((Number) rs.getObject(1)).longValue();
            }
            //查询完成后设置查询到的总数
            model.setTotalCount(count);
        } catch (SQLException e) {
            LogUtils.e("PageInterceptor", "can't get count " + e.getMessage());
        } finally {
            JdbcUtils.close(rs, ps);
        }
        ////利用反射设置当前BoundSql对应的sql属性为我们建立好的分页Sql语句
        BeanPropertyUtils.setFieldValue(boundSql,"sql",model.pageSql(sql));
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
         /* 当目标类是StatementHandler类型时，才包装目标类，不做无谓的代理 */
        return (target instanceof StatementHandler) ? Plugin.wrap(target, this) : target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
