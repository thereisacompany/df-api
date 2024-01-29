package com.dfrecvcle.dfsystem.service.log;

import com.dfrecvcle.dfsystem.exception.HfException;
import com.dfrecvcle.dfsystem.live.entities.Log;
import com.dfrecvcle.dfsystem.live.mappers.LogMapper;
import com.dfrecvcle.dfsystem.service.redis.RedisService;
import com.dfrecvcle.dfsystem.service.user.UserService;
import com.dfrecvcle.dfsystem.utils.Tools;
import com.dfrecvcle.dfsystem.utils.datasource.DataSourceContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.dfrecvcle.dfsystem.utils.Tools.getLocalIp;

@Service
public class LogService {

    private Logger logger = LoggerFactory.getLogger(LogService.class);

    @Resource
    private LogMapper logMapper;

    @Resource
    private UserService userService;

    @Resource
    private RedisService redisService;

    public void insertLog(String moduleName, String content, HttpServletRequest request)throws Exception{
        try{
            DataSourceContextHolder.setDBType("live");
            Long userId = userService.getUserId(request);
            if(userId!=null) {
                String clientIp = getLocalIp(request);
                String createTime = Tools.getNow3();
                Long count = logMapper.getCountByIpAndDate(userId, moduleName, clientIp, createTime);
                if(count > 0) {
                    //如果某個用戶某個IP在同1秒內連續操作兩遍，此時需要刪除該redis記錄，使其退出，防止惡意攻擊
                    redisService.deleteObjectByUserAndIp(userId, clientIp);
                } else {
                    Log log = new Log();
                    log.setUserId(userId);
                    log.setOperation(moduleName);
                    log.setClientIp(getLocalIp(request));
                    log.setCreateAt(new Date());
                    Byte status = 1;
                    log.setStatus(status);
                    log.setContent(content);
                    logMapper.insertSelective(log);
                }
            }
            DataSourceContextHolder.clearDBType();
        }catch(Exception e){
            HfException.writeFail(logger, e);
        }
    }

    public void insertLogWithUserId(Long userId, String moduleName, String content, HttpServletRequest request)throws Exception{
        try{
            if(userId!=null) {
                Log log = new Log();
                log.setUserId(userId);
                log.setOperation(moduleName);
                log.setClientIp(getLocalIp(request));
                log.setCreateAt(new Date());
                Byte status = 1;
                log.setStatus(status);
                log.setContent(content);
                logMapper.insert(log);
            }
        }catch(Exception e){
            HfException.writeFail(logger, e);
        }
    }
}
