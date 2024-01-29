package com.dfrecvcle.dfsystem.utils;

public interface ExceptionCodeConstants {
    /**
     * 用戶錯誤碼定義
     */
    public class UserExceptionCode {
        /**
         * 用戶不存在
         */
        public static final int USER_NOT_EXIST = 1;

        /**
         * 用戶密碼錯誤
         */
        public static final int USER_PASSWORD_ERROR = 2;

        /**
         * 用戶被停用或刪除
         */
        public static final int BLACK_USER = 3;

        /**
         * 可以登入
         */
        public static final int USER_CONDITION_FIT = 4;

        /**
         * 訪問資料庫異常
         */
        public static final int USER_ACCESS_EXCEPTION = 5;
    }
}
