package com.dfrecvcle.dfsystem.utils.datasource;

public class DataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDBType(String dbType) {
        contextHolder.set(dbType);
    }

    public static String getDBType() {
        return contextHolder.get();
    }

    public static void clearDBType() {
        contextHolder.remove();
    }
}
