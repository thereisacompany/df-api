package com.dfrecvcle.dfsystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.dfrecvcle.dfsystem.constants.BusinessConstants;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.exception.BusinessRunTimeException;
import com.dfrecvcle.dfsystem.live.entities.LoginUser;
import com.dfrecvcle.dfsystem.live.entities.UpdPw;
import com.dfrecvcle.dfsystem.live.entities.User;
import com.dfrecvcle.dfsystem.live.entities.UserEx;
import com.dfrecvcle.dfsystem.service.log.LogService;
import com.dfrecvcle.dfsystem.service.redis.RedisService;
import com.dfrecvcle.dfsystem.service.user.UserService;
import com.dfrecvcle.dfsystem.utils.BaseResponseInfo;
import com.dfrecvcle.dfsystem.utils.ExceptionCodeConstants;
import com.dfrecvcle.dfsystem.utils.HfInfo;
import com.dfrecvcle.dfsystem.utils.Tools;
import com.dfrecvcle.dfsystem.utils.datasource.DataSourceContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.dfrecvcle.dfsystem.utils.ResponseJsonUtil.returnJson;

@RestController
@RequestMapping(value = "/user")
@Api(tags = {"用戶管理(後端登入使用)"})
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;

    @Resource
    private RedisService redisService;

    private static String SUCCESS = "操作成功";
    private static String ERROR = "操作失敗";

    @PostMapping(value = "/sendMsg")
    @ApiOperation(value = "發送簡訊")
    public Object sendMsg(@RequestParam String phone, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        // 串接牛訊雲驗證碼
        result.put("verify_code", userService.getVerificationCode(phone));

        return result;
    }

    @PostMapping(value = "/registerUser")
    @ApiOperation(value = "註冊用戶")
    public Object registerUser(@RequestBody UserEx userEx, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
//        userService.checkPhone(userEx);
        userService.registerUser(userEx, request);

        return result;
    }

    @PostMapping(value = "/updateUser")
    @ApiOperation(value = "更新用戶")
    @ResponseBody
    public Object updateUser(@RequestBody UserEx userEx, HttpServletRequest request) throws Exception {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
//        UserEx userEx = JSONObject.parseObject(obj.toJSONString(), UserEx.class);
        userService.updateUser(userEx, request);
        return result;
    }

    @PutMapping(value = "/updatePwd")
    @ApiOperation(value = "更新密碼")
    public String updatePwd(@RequestBody UpdPw jsonObject, HttpServletRequest request) {
        Integer flag = 0;
        Map<String, Object> objectMap = new HashMap<>();
        try {
            DataSourceContextHolder.setDBType("live");
            String info = "";
            Long userId = jsonObject.getUserId();
            String oldpwd = jsonObject.getOldpassword();
            String password = jsonObject.getPassword();
            User user = userService.getUser(userId);
            //必須和原始密碼一致才可以更新密碼
            if ((oldpwd != null && oldpwd.equalsIgnoreCase(user.getPassword())) || user.getPwCode()!=null) {
                user.setPassword(password);
                user.setPwCode(null);
                flag = userService.updateUserByObj(user); //1-成功
                info = "修改成功";
            } else {
                flag = 2; //原始密碼輸入錯誤
                info = "原始密碼輸入錯誤";
            }
            objectMap.put("status", flag);
            if(flag > 0) {
                return returnJson(objectMap, info, HfInfo.OK.code);
            } else {
                return returnJson(objectMap, ERROR, HfInfo.ERROR.code);
            }
        } catch (Exception e) {
            logger.error(">>>>>>>>>>>>>修改用户ID為 ： " + jsonObject.getUserId() + "密碼資訊失敗", e);
            flag = 3;
            objectMap.put("status", flag);
            return returnJson(objectMap, ERROR, HfInfo.ERROR.code);
        }
    }

    @GetMapping(value = "/getAllUsers")
    @ApiOperation(value = "取得會員列表(後台使用)")
    public BaseResponseInfo getAllUsers(@RequestParam(value = "number", required = false) String number,
                                        @RequestParam(value = "alias", required = false) String alias,
                                        @RequestParam("currentPage") Integer currentPage,
                                        @RequestParam("pageSize") Integer pageSize,
                                        HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        Map<String, Object> map = new HashMap<>();
        try {
            DataSourceContextHolder.setDBType("live");

            map.put("rows", userService.getAllUsers(number, alias, currentPage, pageSize));
            map.put("total", userService.countAllUsers(number, alias));

            res.code = 200;
            res.result = map;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.result= "取得user失敗";
        }

        return res;
    }

    @GetMapping(value = "/getUserBySession")
    @ApiOperation(value = "取得會員資料")
    public BaseResponseInfo getSessionUser(HttpServletRequest request)throws Exception {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            DataSourceContextHolder.setDBType("live");
            Map<String, Object> data = new HashMap<>();
            Long userId = Long.parseLong(redisService.getObjectFromSessionByKey(request,"userId").toString());
            UserEx user = userService.getUserEx(userId);
            if(user != null) {
                Object userSig = redisService.getObjectFromSessionByKey(request, "userSig");
                if(userSig != null) {
                    user.setUserSig(String.valueOf(userSig));
                }
                user.setPassword(null);
            }
            data.put("user", user);
            res.code = 200;
            res.result = data;
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.result= "取得user失敗";
        }
        return res;
    }

    @Deprecated
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "取得會員資料")
    public BaseResponseInfo getUser(@PathVariable("id") long id, HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            DataSourceContextHolder.setDBType("live");
            Map<String, Object> data = new HashMap<>();
            UserEx user = userService.getUserEx(id);
            if(user != null) {
                Object userSig = redisService.getObjectFromSessionByKey(request, "userSig");
                if(userSig != null) {
                    user.setUserSig(String.valueOf(userSig));
                }
                user.setPassword(null);
            }
            data.put("user", user);
            res.code = 200;
            res.result = data;
        } catch (Exception e) {
            e.printStackTrace();
            res.code = 500;
            res.result = "取得user失敗";
        }
        return res;
    }

    @PostMapping(value = "/login")
    @ApiOperation(value = "登入")
    public BaseResponseInfo login(@RequestBody LoginUser userParam, HttpServletRequest request) {
        logger.info("============用户登入 login 方法調用開始==============");
        String msgTip = "";
        User user = null;
        BaseResponseInfo res = new BaseResponseInfo();

        try {
            String loginName = userParam.getUsername().trim();
            String password = userParam.getPassword().trim();
            DataSourceContextHolder.setDBType("live");
            // TODO 判斷用户是否已经登入過，登入過不再處理
            Object userId = redisService.getObjectFromSessionByKey(request,"userId");
            if (userId != null) {
                logger.info("====用戶已經登入過, login 方法調用結束====");
                msgTip = "user already login";
            }
            System.out.println("??????");
            //獲取用戶狀態
            int userStatus = -1;
            try {
                redisService.deleteObjectBySession(request, "userId");
                userStatus = userService.validateUser(loginName, password);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(">>>>>>>>>>>>>用户  " + loginName + " 登入 login 方法 訪問服務層異常====", e);
                msgTip = "access service exception";
            }

            String token = UUID.randomUUID().toString().replaceAll("-", "") + "";
            switch (userStatus) {
                case ExceptionCodeConstants.UserExceptionCode.USER_NOT_EXIST:
                    msgTip = "user is not exist";
                    break;
                case ExceptionCodeConstants.UserExceptionCode.USER_PASSWORD_ERROR:
                    msgTip = "user password error";
                    break;
                case ExceptionCodeConstants.UserExceptionCode.BLACK_USER:
                    msgTip = "user is black";
                    break;
                case ExceptionCodeConstants.UserExceptionCode.USER_ACCESS_EXCEPTION:
                    msgTip = "access service error";
                    break;
                case ExceptionCodeConstants.UserExceptionCode.USER_CONDITION_FIT:
                    msgTip = "user can login";
                    //驗證通過 ，可以登入，放入session，記錄登入日誌
                    user = userService.getUserByLoginName(loginName);

                    Object userSig = redisService.getObjectFromSessionByKey(request, "userSig");
                    if(userSig != null) {
                        Object userSigExpire = redisService.getObjectFromSessionByKey(request, "userSigExpire");
                        if(userSigExpire != null) {
                            LocalDate expire = LocalDate.parse(String.valueOf(userSigExpire));
                            if(LocalDate.now().isAfter(expire)) { // 過期重拿
                                userSig = userService.genUserSig(user.getUsername(), 180);
                            }
                        } else { // 未存放過期日
                            redisService.storageObjectBySession(token, "userSigExpire", LocalDate.now().plusDays(1));
                        }
                        user.setUserSig(String.valueOf(userSig));
                    } else {
                        user.setUserSig(userService.genUserSig(user.getUsername(), 180));
                        redisService.storageObjectBySession(token, "userSigExpire", LocalDate.now().plusDays(180));
                    }
                    redisService.storageObjectBySession(token, "userSig", user.getUserSig());

                    // TODO redis service
                    redisService.storageObjectBySession(token,"userId", user.getId());

                    break;
                default:
                    break;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("msgTip", msgTip);
            if(user != null){
                // TODO redis service
                redisService.storageObjectBySession(token, "roleType", 1);
                redisService.storageObjectBySession(token, "clientIp", Tools.getLocalIp(request));

                // log service
                logService.insertLogWithUserId(user.getId(), "用户",
                        new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_LOGIN).append(user.getUsername()).toString(),
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());

                data.put("token", token);
                data.put("user", user);

                //用户的按鈕權限
                //                JSONArray btnStrArr = userService.getBtnStrArrById(user.getId());
//                if(!"admin".equals(user.getLoginName())){
//                    data.put("userBtn", btnStrArr);
//                }
//                data.put("roleType", roleType);
            }

            res.code = 200;
            res.result = data;
            logger.info("===============用户登入 login 方法調用结束===============");
        } catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            res.code = 500;
            res.result = "用户登入失敗";
        }

        return res;
    }

    @GetMapping(value = "/logout")
    @ApiOperation(value = "登出")
    public BaseResponseInfo logout(HttpServletRequest request, HttpServletResponse response) {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
//            DataSourceContextHolder.setDBType("live");
            // TODO 實作 redis
            redisService.deleteObjectBySession(request, "userId");
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.result = "退出失敗";
        }
        return res;
    }

    @GetMapping(value = "/getUserSig")
    @ApiOperation(value = "未登入時取得預設帳號的userSig (請小心使用)")
    public BaseResponseInfo getUserSig(HttpServletRequest request) {
        BaseResponseInfo res = new BaseResponseInfo();
        try {
            res.code = 200;
            res.result = userService.genUserSig(null, 1);
        } catch(Exception e){
            e.printStackTrace();
            res.code = 500;
            res.result = "取得失敗";
        }
        return res;
    }

    @PostMapping(value = "/forget-password/sendMsg")
    @ApiOperation(value = "忘記密碼-發送驗證碼")
    public Object forgetPasswordSendMsg(@RequestParam String phone, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();
        DataSourceContextHolder.setDBType("live");
        // 串接牛訊雲驗證碼
        result.put("verify_code", userService.getForgetPasswordCode(phone));
        return result;
    }

    @GetMapping(value = "/forget-password")
    @ApiOperation(value = "忘記密碼-送出")
    public Object forgetPassword(@RequestParam String phone, @RequestParam Integer code, HttpServletRequest request) {
        JSONObject result = ExceptionConstants.standardSuccess();

        DataSourceContextHolder.setDBType("live");
        if (userService.checkPhoneAndCode(phone, code, result).isEmpty()) {
            logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.FORGET_PASSWORD_PHONE_OR_CODE_ERROR_CODE,
                    ExceptionConstants.FORGET_PASSWORD_PHONE_OR_CODE_ERROR_MSG, phone);
            throw new BusinessRunTimeException(ExceptionConstants.FORGET_PASSWORD_PHONE_OR_CODE_ERROR_CODE,
                    ExceptionConstants.FORGET_PASSWORD_PHONE_OR_CODE_ERROR_MSG);
        }

        return result;
    }
}
