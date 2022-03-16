package com.open.qa.common;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.SQLException;

/**
 * 动态数据源
 * @author : liang.chen
 * create in 2018/7/26 下午3:53
 */
public class DruidDataSourceFactory {


    /**
     * 构建一个简单的数据源
     * @param args
     * @return
     */
    public static DruidDataSource getSimpleDruidDataSource(String ... args) throws SQLException{
        DruidDataSource dataSource = new DruidDataSource();
        if (args.length < 3){
            throw  new IllegalArgumentException("参数错误，请至少输入url，username，password三个配置!");
        }else if (args.length == 3){
            dataSource.setUrl(args[0]);
            dataSource.setUsername(args[1]);
            dataSource.setPassword(args[2]);
        }else {
            dataSource.setUrl(args[0]);
            dataSource.setUsername(args[1]);
            dataSource.setPassword(args[2]);
            dataSource.setMaxActive(Integer.parseInt(args[3]));

        }
        dataSource.setInitialSize(2);
        dataSource.setMinIdle(2);
        dataSource.setTimeBetweenEvictionRunsMillis(10000);
        dataSource.setMinEvictableIdleTimeMillis(60000);
        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(1800);
        dataSource.setLogAbandoned(true);
        dataSource.setFilters("stat");
        dataSource.setConnectionProperties("druid.stat.slowSqlMillis=100");
        return dataSource;
    }

}
