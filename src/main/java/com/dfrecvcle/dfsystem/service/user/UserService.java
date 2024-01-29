package com.dfrecvcle.dfsystem.service.user;

import com.alibaba.fastjson.JSONObject;
import com.dfrecvcle.dfsystem.constants.BusinessConstants;
import com.dfrecvcle.dfsystem.constants.ExceptionConstants;
import com.dfrecvcle.dfsystem.exception.BusinessRunTimeException;
import com.dfrecvcle.dfsystem.exception.HfException;
import com.dfrecvcle.dfsystem.live.entities.User;
import com.dfrecvcle.dfsystem.live.entities.UserEx;
import com.dfrecvcle.dfsystem.live.mappers.UserMapper;
import com.dfrecvcle.dfsystem.live.mappers.UserMapperEx;
import com.dfrecvcle.dfsystem.service.log.LogService;
import com.dfrecvcle.dfsystem.service.redis.RedisService;
import com.dfrecvcle.dfsystem.tencentyun.TLSSigAPIv2;
import com.dfrecvcle.dfsystem.utils.ExceptionCodeConstants;
import com.dfrecvcle.dfsystem.utils.NexmoSmsSender;
import com.dfrecvcle.dfsystem.utils.Tools;
import com.dfrecvcle.dfsystem.utils.datasource.DataSourceContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserMapperEx userMapperEx;

    @Resource
    private NexmoSmsSender nexmoSmsSender;

    @Resource
    private LogService logService;

    @Resource
    private RedisService redisService;

    @Value("${tencentyun.sdkappid}")
    private long sdkAppId;

    @Value("${tencentyun.key}")
    private String key;

    @Value("${tencentyun.identifier}")
    private String userId;

    @Value("${tencentyun.expire}")
    private long expire;

    public int getVerificationCode(String phone) {
        if(phone.replaceFirst("84", "").length() < 9
                || phone.replaceFirst("886","").length() < 9 || (phone.length() < 11 || phone.length() > 12)) {
            logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.USER_VERIFY_FORMAT_ERROR_CODE,
                    ExceptionConstants.USER_VERIFY_FORMAT_ERROR_MSG, phone);
            throw new BusinessRunTimeException(ExceptionConstants.USER_VERIFY_FORMAT_ERROR_CODE,
                    ExceptionConstants.USER_VERIFY_FORMAT_ERROR_MSG);
        }

        List<User> list = getUserByPhone(phone, null);
        if(list.isEmpty()) {
            int code = getTestCode();

            String expired = LocalDateTime.now().plusMinutes(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // insert user
            UserEx userEx = new UserEx();
            userEx.setPhone(phone);
            userEx.setCode(code);
            userEx.setCodeExpired(expired);
            userEx.setPhoneValid((byte) 0);
            userEx.setTelegramValid((byte) 0);

            userMapperEx.replaceUserEx(userEx);
            nexmoSmsSender.SendSms(phone, code, 0);

            return code;
        } else {
//            User user = list.get(0);
            // update
            logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.USER_PHONE_ALREADY_EXISTS_CODE,
                    ExceptionConstants.USER_PHONE_ALREADY_EXISTS_MSG, phone);
            throw new BusinessRunTimeException(ExceptionConstants.USER_PHONE_ALREADY_EXISTS_CODE,
                    ExceptionConstants.USER_PHONE_ALREADY_EXISTS_MSG);
        }
    }

    private void checkPhone(UserEx userEx) {
        if(userEx == null || userEx.getPhone() == null) {
            logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.USER_ADD_FAILED_CODE,
                    ExceptionConstants.USER_ADD_FAILED_MSG);
            throw new BusinessRunTimeException(ExceptionConstants.USER_ADD_FAILED_CODE,
                    ExceptionConstants.USER_ADD_FAILED_MSG);
        }

//        if(userEx.getPhone().indexOf("84")==0) {
            String myPhone = userEx.getPhone();//.replaceFirst("84", "");
            if(!userEx.getUsername().equals(myPhone)) {
                logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.USER_REGISTER_USERNAME_PHONE_DIFF_CODE,
                        ExceptionConstants.USER_REGISTER_USERNAME_PHONE_DIFF_MSG, userEx.getPhone());
                throw new BusinessRunTimeException(ExceptionConstants.USER_REGISTER_USERNAME_PHONE_DIFF_CODE,
                        ExceptionConstants.USER_REGISTER_USERNAME_PHONE_DIFF_MSG);
            }
//        }

//        if(userEx.getPhone().indexOf("886")==0) {
//            String twPhone = userEx.getPhone().replaceFirst("886", "");
//            if(!userEx.getUsername().equals(twPhone)) {
//                logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.USER_REGISTER_USERNAME_PHONE_DIFF_CODE,
//                        ExceptionConstants.USER_REGISTER_USERNAME_PHONE_DIFF_MSG, userEx.getPhone());
//                throw new BusinessRunTimeException(ExceptionConstants.USER_REGISTER_USERNAME_PHONE_DIFF_CODE,
//                        ExceptionConstants.USER_REGISTER_USERNAME_PHONE_DIFF_MSG);
//            }
//        }

        List<User> list = getUserByPhone(userEx.getPhone(), "valid");
        if(!list.isEmpty()) {
            logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.USER_PHONE_ALREADY_EXISTS_CODE,
                    ExceptionConstants.USER_PHONE_ALREADY_EXISTS_MSG, userEx.getPhone());
            throw new BusinessRunTimeException(ExceptionConstants.USER_PHONE_ALREADY_EXISTS_CODE,
                    ExceptionConstants.USER_PHONE_ALREADY_EXISTS_MSG);
        }
    }

    public void checkUsernameAndAlias(UserEx userEx) {
        List<User> list = null;
        if(userEx == null){
            return;
        }
        Long userId = userEx.getId();
        if(!StringUtils.isEmpty(userEx.getUsername())) {
            String loginName = userEx.getUsername();
            list = getUserListByUserName(loginName);
            if(list != null && !list.isEmpty()) {
                if(list.size() > 1) {
                    // 超過一筆資料存在，該登錄名稱存在
                    logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.USER_USER_NAME_ALREADY_EXISTS_CODE,
                            ExceptionConstants.USER_USER_NAME_ALREADY_EXISTS_MSG);
                    throw new BusinessRunTimeException(ExceptionConstants.USER_USER_NAME_ALREADY_EXISTS_CODE,
                            ExceptionConstants.USER_USER_NAME_ALREADY_EXISTS_MSG);
                }
                if(list.size() == 1) {
                    if(userId == null || (userId != null && !!userId.equals(list.get(0).getId()))) {
                        logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.USER_USER_NAME_ALREADY_EXISTS_CODE,
                                ExceptionConstants.USER_USER_NAME_ALREADY_EXISTS_MSG);
                        throw new BusinessRunTimeException(ExceptionConstants.USER_USER_NAME_ALREADY_EXISTS_CODE,
                                ExceptionConstants.USER_USER_NAME_ALREADY_EXISTS_MSG);
                    }
                }
            }
        }
        if(!StringUtils.isEmpty(userEx.getAlias())) {
            String alias = userEx.getAlias();
            list = getUserListByAlias(alias);
            if(list != null && !list.isEmpty()) {
                if(list.size() > 1) {
                    // 超過一筆資料存在，該登錄名稱存在
                    logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.USER_ALIAS_ALREADY_EXISTS_CODE,
                            ExceptionConstants.USER_ALIAS_ALREADY_EXISTS_MSG);
                    throw new BusinessRunTimeException(ExceptionConstants.USER_ALIAS_ALREADY_EXISTS_CODE,
                            ExceptionConstants.USER_ALIAS_ALREADY_EXISTS_MSG);
                }
                if(list.size() == 1) {
                    if(userId == null || (userId != null && !!userId.equals(list.get(0).getId()))) {
                        logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.USER_ALIAS_ALREADY_EXISTS_CODE,
                                ExceptionConstants.USER_ALIAS_ALREADY_EXISTS_MSG);
                        throw new BusinessRunTimeException(ExceptionConstants.USER_ALIAS_ALREADY_EXISTS_CODE,
                                ExceptionConstants.USER_ALIAS_ALREADY_EXISTS_MSG);
                    }
                }
            }
        }
    }

    public User getUser(long id)throws Exception {
        User result = null;
        try{
            DataSourceContextHolder.setDBType("live");
            result = userMapper.selectByPrimaryKey(id);
            DataSourceContextHolder.clearDBType();
        }catch(Exception e){
            HfException.readFail(logger, e);
        }
        return result;
    }

    public UserEx getUserEx(long id) {
        UserEx result = null;
        try{
            result = userMapperEx.getUserById(id);
        }catch (Exception e) {
            HfException.readFail(logger, e);
        }
        return result;
    }

    public int validateUser(String loginName, String password) throws Exception {
        List<User> list = null;

        try {
            list = userMapper.selectByUsernameOrPassword(loginName, null);
            if (list != null && list.isEmpty()) {
                return ExceptionCodeConstants.UserExceptionCode.USER_NOT_EXIST;
            } else if (list.size() == 1) {
                if(list.get(0).getStatus() != 1) {
                    return ExceptionCodeConstants.UserExceptionCode.BLACK_USER;
                }
            }
        } catch (Exception e) {
            logger.error(">>>>>>>>訪問驗證用戶姓名是否存在後台信息異常", e);
            return ExceptionCodeConstants.UserExceptionCode.USER_ACCESS_EXCEPTION;
        }

        try {
            list = userMapper.selectByUsernameOrPassword(loginName, password);
            if (list != null && list.isEmpty()) {
                return ExceptionCodeConstants.UserExceptionCode.USER_PASSWORD_ERROR;
            }
        } catch (Exception e) {
            logger.error(">>>>>>>>>>訪問驗證用戶密碼後台信息異常", e);
            return ExceptionCodeConstants.UserExceptionCode.USER_ACCESS_EXCEPTION;
        }

        return ExceptionCodeConstants.UserExceptionCode.USER_CONDITION_FIT;
    }

    public User getUserByLoginName(String loginName)throws Exception {
        List<User> list = null;
        try{
            list = userMapper.selectByUsernameOrPassword(loginName, null);
        }catch(Exception e){
            HfException.readFail(logger, e);
        }
        User user = null;
        if(list != null && !list.isEmpty()){
            user = list.get(0);
        }
        return user;
    }

    /**
     * TODO 產生 UserSig
     * @param userId
     * @return
     */
    public String genUserSig(String userId, int days) {
        TLSSigAPIv2 tlsSigAPIv2 = new TLSSigAPIv2(sdkAppId, key);
        if(userId == null) {
            userId = this.userId;
        }
        return tlsSigAPIv2.genUserSig(userId, days*expire);
    }

    public String getUserId() {
        return userId;
    }

    public Long getUserId(HttpServletRequest request) {
        Object userIdObj = redisService.getObjectFromSessionByKey(request,"userId");
        Long userId = null;
        if(userIdObj != null) {
            userId = Long.parseLong(userIdObj.toString());
        }
        return userId;
    }

    public int getForgetPasswordCode(String phone) {
        List<User> list = getUserByPhone(phone, null);
        if(list.isEmpty()) {
            logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.FORGET_PASSWORD_SENDMSG_NO_USER_CODE,
                    ExceptionConstants.FORGET_PASSWORD_SENDMSG_NO_USER_MSG, phone);
            throw new BusinessRunTimeException(ExceptionConstants.FORGET_PASSWORD_SENDMSG_NO_USER_CODE,
                    ExceptionConstants.FORGET_PASSWORD_SENDMSG_NO_USER_MSG);
        } else {
            User user = list.get(0);

            int forgetPWCode = getTestCode();
            nexmoSmsSender.SendSms(phone, forgetPWCode, 1);

            // update
            userMapper.updateForgetPwCode(user.getId(), forgetPWCode);
            return forgetPWCode;
        }
    }

    public String checkPhoneAndCode(String phone, int code, JSONObject res) {
        List<User> list = getUserByPhone(phone, null);
        if(list.isEmpty()) {
            logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.FORGET_PASSWORD_SENDMSG_NO_USER_CODE,
                    ExceptionConstants.FORGET_PASSWORD_SENDMSG_NO_USER_MSG, phone);
            throw new BusinessRunTimeException(ExceptionConstants.FORGET_PASSWORD_SENDMSG_NO_USER_CODE,
                    ExceptionConstants.FORGET_PASSWORD_SENDMSG_NO_USER_MSG);
        }

//        if(phone.indexOf("886")==0) {
//            phone = phone.replaceFirst("886", "");
//        }
//        if(phone.indexOf("84")==0) {
//            phone = phone.replaceFirst("84", "");
//        }
        User user = list.get(0);
        if(user != null) {
            res.put("user_id", user.getId());
        }
        return userMapper.checkPhoneAndCode(phone, code);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void registerUser(UserEx userEx, HttpServletRequest request) {

        checkPhone(userEx);

        checkCode(userEx.getPhone(), userEx.getCode());

        userEx.setPhone(userEx.getPhone());
        userEx.setPassword(userEx.getPassword());
        userEx.setStatus(BusinessConstants.USER_STATUS_NORMAL);

        userEx.setUserType((byte) 0);
        userEx.setLevel((byte) 0);
        userEx.setBalance(new BigDecimal(0.0000));
        userEx.setRegisterAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        userEx.setRegisterIp(Tools.getLocalIp(request));

        int result = 0;
        try{
            result = userMapper.insertUser(userEx);
            userEx.setUserId(userEx.getId());
            userEx.setPhoneValid((byte) 1);
        }catch(Exception e){
            HfException.writeFail(logger, e);
        }

        long id = userMapperEx.getUserByPhone(userEx.getPhone());
        userEx.setId(id);
//        System.out.println(">>>"+userEx);
        userMapperEx.updateByPrimaryKeySelective(userEx);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void updateUser(UserEx userEx, HttpServletRequest request) throws Exception {
        checkUsernameAndAlias(userEx);
        int result = 0;
        try{
//            System.out.println(">>>"+userEx);
            if(userEx.getId() == null) {
                userEx.setId(userEx.getUserId());
            }
            result = userMapper.updateByPrimaryKeySelective(userEx);
//            System.out.println("result>>>"+result);
        }catch(Exception e){
            HfException.writeFail(logger, e);
        }
        if(userEx == null) {
            logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.USER_EDIT_FAILED_CODE,
                    ExceptionConstants.USER_EDIT_FAILED_MSG);
            throw new BusinessRunTimeException(ExceptionConstants.USER_EDIT_FAILED_CODE,
                    ExceptionConstants.USER_EDIT_FAILED_MSG);
        }

        // TODO log service
        logService.insertLog("用户",
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(userEx.getUserId()).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateUserByObj(User user) throws Exception{
        int result = 0;
        try{
            result = userMapper.updateByPrimaryKeySelective(user);

            // TODO log service
        logService.insertLog("用户",
                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append(user.getId()).toString(),
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        }catch(Exception e){
            HfException.writeFail(logger, e);
        }
        return result;
    }

    public List<JSONObject> getAllUsers(String phone, String alias, int offset, int rows) {
        List<JSONObject> retList = new ArrayList<>();
        List<UserEx> list = userMapperEx.getAllUserByKey(phone, alias, offset*rows, rows);
        list.stream().forEach(userEx -> {
            JSONObject json = new JSONObject();
            json.put("id", userEx.getId());
            json.put("phone", userEx.getPhone());
            json.put("alias", userEx.getAlias());
            json.put("register_at", userEx.getRegisterAt());
            json.put("status", userEx.getStatus());
            json.put("ban_at", userEx.getBanAt());
            retList.add(json);
        });

        return retList;
    }

    public int countAllUsers(String phone, String alias) {
        return userMapperEx.countAllUserByKey(phone, alias);
    }

    public User getCurrentUser()throws Exception{
        HttpServletRequest request = ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Long userId = Long.parseLong(redisService.getObjectFromSessionByKey(request, "userId").toString());
        return getUser(userId);
    }

    private void checkCode(String phone, int code) {
        String codeExpired = userMapperEx.getPhoneAndCode(phone, code);
        if(codeExpired == null || codeExpired.isEmpty()) {
            // TODO
            logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.USER_VERIFY_CODE_NOT_EXISTS_CODE,
                    ExceptionConstants.USER_VERIFY_CODE_NOT_EXISTS_MSG);
            throw new BusinessRunTimeException(ExceptionConstants.USER_VERIFY_CODE_NOT_EXISTS_CODE,
                    ExceptionConstants.USER_VERIFY_CODE_NOT_EXISTS_MSG);
        } else {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expired = LocalDateTime.parse(codeExpired, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if(now.isAfter(expired)) {
                // TODO
                logger.error("異常碼[{}],異常提示[{}],参數,[{}]", ExceptionConstants.USER_VERIFY_CODE_EXPIRED_CODE,
                        ExceptionConstants.USER_VERIFY_CODE_EXPIRED_MSG);
                throw new BusinessRunTimeException(ExceptionConstants.USER_VERIFY_CODE_EXPIRED_CODE,
                        ExceptionConstants.USER_VERIFY_CODE_EXPIRED_MSG);
            }
        }
    }

    private List<User> getUserByPhone(String phone, String valid) {
        List<User> list = null;
        try {
            list = userMapperEx.getUserListByPhone(phone, valid);
        } catch (Exception e) {
            HfException.readFail(logger, e);
        }

        return list;
    }

    private List<User> getUserListByUserName(String userName){
        List<User> list = null;
        try{
            list = userMapperEx.getUserListByUserNameOrAlias(userName, null);
        }catch(Exception e){
            HfException.readFail(logger, e);
        }
        return list;
    }

    private List<User> getUserListByAlias(String alias){
        List<User> list = null;
        try{
            list = userMapperEx.getUserListByUserNameOrAlias(alias, null);
        }catch(Exception e){
            HfException.readFail(logger, e);
        }
        return list;
    }

    private Integer getTestCode() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        int randomCode = random.nextInt(max - min + 1) + min;

        return randomCode;
    }
}
