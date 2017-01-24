package com.fblog.core.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by fansion on 18/01/2017.
 * 数据库工具
 */
public class JdbcUtils {

    public  static void  close(Connection connection){
        if (connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void  close(Statement statement){
        if (statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(ResultSet set){
        if (set!=null){
            try {
                set.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(ResultSet rs, Statement sta, Connection conn){
        close(rs);
        close(sta, conn);
    }

    public static void close(Statement sta, Connection conn){
        close(sta);
        close(conn);
    }

    public static void close(ResultSet set, Statement state){
        close(set);
        close(state);
    }
}
