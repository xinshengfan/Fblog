package com.fblog.core.dao.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fansion on 18/01/2017.
 * 数据库分页组件 在service层构造PageModel,添加查询参数insertQuery 在sql中不用写limit语句
 */
public class PageModel<T> {
    private static final int PAGE_SIZE = 10;
    private int pageIndex;
    /* 当pageSize小于0时,为查询所有 */
    private int pageSize;
    //所有文章的总数
    private long totalCount;
    //使用PageInterceptor动态查询出指定index起至pageSize的文章ids
    private List<T> content;
    //查询参数
    private MapContainer query;

    public PageModel(int pageIndex) {
        this(pageIndex, PAGE_SIZE);
    }

    public PageModel(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.query = new MapContainer();
        this.content = new ArrayList<>();
    }

    public int getTotalPage() {
        int pages = (int) (totalCount / pageSize);
        if (totalCount % pageSize != 0) {
            pages++;
        }
        return pages;
    }

    /**
     * 将指定的key和value放入在MapContaner,用于在mapper.xml查询使用
     * 这里在mapper.xml使用query.key来引用放入的的value
     * 注意要放入时的key与mapper.xml引用的key对应上
     */
    public PageModel<T> insertQuery(String key, Object value) {
        query.put(key, value);
        return this;
    }

    public boolean isQueryAll() {
        return pageSize < 1;
    }

    /**
     * 生成查询数量的sql
     */
    public String countSql(String sql) {
        return "select count(*) from (" + sql + ")_temp_";
    }

    /**
     * 生成分布查询的sql
     */
    public String pageSql(String sql) {
        return sql + " limit " + (getPageIndex() - 1) * getPageSize() + "," + getPageSize();
    }

    public void addContent(T mc) {
        this.content.add(mc);
    }

    public Object removeQuery(String key) {
        return query.remove(key);
    }


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public MapContainer getQuery() {
        return query;
    }

    public void setQuery(MapContainer query) {
        this.query = query;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}
