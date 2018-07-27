package com.open.qa.common;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/** 实现动态数据源切换
 * Created by liang.chen on 2017/7/14.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {


    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceType();
    }
}

