package com.dfrecvcle.dfsystem.constants;


import com.alibaba.fastjson.JSONObject;

public class ExceptionConstants {

    public static final String GLOBAL_RETURNS_CODE = "code";
    public static final String GLOBAL_RETURNS_MESSAGE = "msg";

    /**
     * 正常返回/操作成功
     **/
    public static final int SERVICE_SUCCESS_CODE = 200;
    public static final String SERVICE_SUCCESS_MSG = "操作成功";

    /**
     * 數據查詢異常
     */
    public static final int DATA_READ_FAIL_CODE = 300;
    public static final String DATA_READ_FAIL_MSG = "數據查詢異常";
    /**
     * 數據寫入異常
     */
    public static final int DATA_WRITE_FAIL_CODE = 301;
    public static final String DATA_WRITE_FAIL_MSG = "數據寫入異常";

    /**
     * 用户訊息
     * type = 4
     * */
    //新增用户失敗
    public static final int USER_ADD_FAILED_CODE = 400000;
    public static final String USER_ADD_FAILED_MSG = "新增用户失敗";
    public static final int USER_EDIT_FAILED_CODE = 400002;
    public static final String USER_EDIT_FAILED_MSG = "修改用户資訊失敗";
    //用户名已存在
    public static final int USER_ALIAS_ALREADY_EXISTS_CODE = 400003;
    public static final String USER_ALIAS_ALREADY_EXISTS_MSG = "用户名在本系统已存在";
    //登錄名已存在
    public static final int USER_USER_NAME_ALREADY_EXISTS_CODE = 400003;
    public static final String USER_USER_NAME_ALREADY_EXISTS_MSG = "登錄名在本系统已存在";
    //電話已存在
    public static final int USER_PHONE_ALREADY_EXISTS_CODE = 400004;
    public static final String USER_PHONE_ALREADY_EXISTS_MSG = "註冊電話在本系统已存在";
    //驗證碼不存在
    public static final int USER_VERIFY_CODE_NOT_EXISTS_CODE = 400005;
    public static final String USER_VERIFY_CODE_NOT_EXISTS_MSG = "驗證碼錯誤或驗證碼不存在";
    //驗證碼已失效
    public static final int USER_VERIFY_CODE_EXPIRED_CODE = 400006;
    public static final String USER_VERIFY_CODE_EXPIRED_MSG = "驗證碼已失效";
    //此用戶未註冊過
    public static final int FORGET_PASSWORD_SENDMSG_NO_USER_CODE = 400007;
    public static final String FORGET_PASSWORD_SENDMSG_NO_USER_MSG = "此用戶未註冊過";
    //電話或驗證碼有誤
    public static final int FORGET_PASSWORD_PHONE_OR_CODE_ERROR_CODE = 400008;
    public static final String FORGET_PASSWORD_PHONE_OR_CODE_ERROR_MSG = "電話或驗證碼有誤";
    //電話格式有誤
    public static final int USER_VERIFY_FORMAT_ERROR_CODE = 400009;
    public static final String USER_VERIFY_FORMAT_ERROR_MSG = "電話格式有誤";
    //註冊的帳號與電話不符
    public static final int USER_REGISTER_USERNAME_PHONE_DIFF_CODE = 400010;
    public static final String USER_REGISTER_USERNAME_PHONE_DIFF_MSG = "註冊的帳號與電話不符";

    /**
     * 最新消息
     * type = 6
     */
    // 新增最新消息失敗
    public static final int NEWS_ADD_FAILED_CODE = 600000;
    public static final String NEWS_ADD_FAILED_MSG = "新增最新消息失敗(未帶入type或title)";

    // 修改最新消息失敗
    public static final int NEWS_EDIT_FAILED_CODE = 600001;
    public static final String NEWS_EDIT_FAILED_MSG = "修改最新消息失敗(未帶入news id)";

    // 標題已存在
    public static final int NEWS_TITLE_ALREADY_EXIST_CODE = 600002;
    public static final String NEWS_TITLE_ALREADY_EXIST_MSG = "標題在本系統已存在";

    /**
     * 意見回饋
     * type = 7
     */
    // 新增意見回饋失敗
    public static final int FEEDBACK_ADD_FAILED_CODE = 700000;
    public static final String FEEDBACK_ADD_FAILED_MSG = "新增意見回饋失敗(未帶入姓名或內容)";

    // 回復意見回饋失敗
    public static final int FEEDBACK_EDIT_FAILED_CODE = 700001;
    public static final String FEEDBACK_EDIT_FAILED_MSG = "回復意見回饋失敗(未帶入feedback id或回復內容)";

    // 查無資料
    public static final int FEEDBACK_ID_NO_DATA_CODE = 700002;
    public static final String FEEDBACK_ID_NO_DATA_MSG = "查無此id(%s)的意見";


    /**
     * 文章
     * type = 9
     */
    // 新增關鍵字標籤失敗
    public static final int ARTICLE_TAG_ADD_FAILED_CODE = 900000;
    public static final String ARTICLE_TAG_ADD_FAILED_MSG = "新增關鍵字標籤失敗(未帶入標籤名稱)";
    // 修改關鍵字標籤失敗
    public static final int ARTICLE_TAG_EDIT_FAILED_CODE = 900001;
    public static final String ARTICLE_TAG_EDIT_FAILED_MSG = "編輯關鍵字標籤失敗(未帶入關鍵字標籤id或查無此筆資料)";
    // 新增、修改關鍵字標籤名字已存在
    public static final int ARTICLE_TAG_IS_EXIST_CODE = 900002;
    public static final String ARTICLE_TAG_IS_EXIST_MSG = "新增、修改關鍵字標籤(%s)名字已存在";
    // 新增文章分類失敗
    public static final int ARTICLE_CATEGORY_ADD_FAILED_CODE = 900010;
    public static final String ARTICLE_CATEGORY_ADD_FAILED_MSG = "新增文章分類失敗(未帶入分類名稱或代稱)";
    // 修改文章分類失敗
    public static final int ARTICLE_CATEGORY_EDIT_FAILED_CODE = 900011;
    public static final String ARTICLE_CATEGORY_EDIT_FAILED_MSG = "編輯文章分類失敗(未帶入文章分類id或查無此筆資料)";
    // 新增、修改文章分類名字已存在
    public static final int ARTICLE_CATEGORY_IS_EXIST_CODE = 900012;
    public static final String ARTICLE_CATEGORY_IS_EXIST_MSG = "新增、修改文章分類(%s)名字已存在";
    // 新增文章失敗
    public static final int ARTICLE_ADD_FAILED_CODE = 900020;
    public static final String ARTICLE_ADD_FAILED_MSG = "新增文章失敗(未帶入標題或代稱)";
    // 修改文章失敗
    public static final int ARTICLE_EDIT_FAILED_CODE = 900021;
    public static final String ARTICLE_EDIT_FAILED_MSG = "編輯文章失敗(未帶入文章id或查無此筆資料)";
    // 新增、修改文章標題已存在
    public static final int ARTICLE_TITLE_IS_EXIST_CODE = 900022;
    public static final String ARTICLE_TITLE_IS_EXIST_MSG = "新增、修改文章標題(%s)名字已存在";

    public static JSONObject standardSuccess () {
        JSONObject success = new JSONObject();
        success.put(GLOBAL_RETURNS_CODE, SERVICE_SUCCESS_CODE);
        success.put(GLOBAL_RETURNS_MESSAGE, SERVICE_SUCCESS_MSG);
        return success;
    }
}
