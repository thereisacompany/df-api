package com.dfrecvcle.dfsystem.constants;

public class BusinessConstants {
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String GCS_UPLOAD_BUCKET_URL = "https://storage.googleapis.com/";

    public static final String DF_UPLOAD_BUCKET_URL = "https://storage.googleapis.com/";

    /**
     * 用户狀態
     * 0：停用，1：啟用，2刪除
     * */
    public static final byte USER_STATUS_BANNED = 0;
    public static final byte USER_STATUS_NORMAL = 1;
    public static final byte USER_STATUS_DELETE = 2;


    public static final String LOG_OPERATION_TYPE_LOGIN = "登入";

    public static final String LOG_OPERATION_TYPE_ADD = "新增";
    public static final String LOG_OPERATION_TYPE_EDIT = "修改";
    public static final String LOG_OPERATION_TYPE_DELETE = "刪除";

    /**
     * redis相關
     * */
    //session的生命周期,秒
    public static final Long MAX_SESSION_IN_SECONDS=60*60*24L;

}
