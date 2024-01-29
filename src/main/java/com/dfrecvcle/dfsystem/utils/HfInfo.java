package com.dfrecvcle.dfsystem.utils;

public enum HfInfo {

    OK(200, "成功"),
    ERROR(500, "服務內部錯誤");

    public final int code;
    public final String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * 定義枚舉構造函數
     */
    HfInfo(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
