package com.dfrecvcle.dfsystem.service.redis;

import com.dfrecvcle.dfsystem.constants.BusinessConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisService {

    @Resource
    public RedisTemplate redisTemplate;

    public static final String ACCESS_TOKEN = "X-Access-Token";

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

    /**
     * description:
     *  從session中獲取訊息
     *@date: 2021/1/28 18:10
     * @Param: request
     * @Param: key
     * @return Object
     */
    public Object getObjectFromSessionByKey(HttpServletRequest request, String key){
        Object obj=null;
        if(request==null){
            return null;
        }
        String token = request.getHeader(ACCESS_TOKEN);
        System.out.println(">>>>>>>>"+token);
        if(token!=null) {
            //開啓redis，用戶數據放在redis中，從redis中獲取
            if(redisTemplate.opsForHash().hasKey(token,key)){
                System.out.println(">>>>>>1>>");
                //redis中存在，拿出來使用
                obj=redisTemplate.opsForHash().get(token,key);
                redisTemplate.expire(token, BusinessConstants.MAX_SESSION_IN_SECONDS, TimeUnit.SECONDS);
            }
        }
        return obj;
    }
    /**
     * description:
     *  將訊息放入session或者redis中
     *@date: 2021/1/28 18:10
     * @Param: request
     * @Param: key
     * @Param: obj
     * @return
     */
    public void storageObjectBySession(String token, String key, Object obj) {
        //開啓redis，用戶數據放到redis中
        redisTemplate.opsForHash().put(token, key, obj.toString());
        redisTemplate.expire(token, BusinessConstants.MAX_SESSION_IN_SECONDS, TimeUnit.SECONDS);
    }
    /**
     * description:
     *  將訊息從session或者redis中移除
     *@date: 2021/1/28 18:10
     * @Param: request
     * @Param: key
     * @Param: obj
     * @return
     */
    public void deleteObjectBySession(HttpServletRequest request, String key){
        if(request!=null){
            String token = request.getHeader(ACCESS_TOKEN);
            if(token != null && !token.isEmpty()){
                System.out.println("redisTemplate>>>"+redisTemplate);
                //開啓redis，用戶數據放在redis中，從redis中刪除
                redisTemplate.opsForHash().delete(token, key);
            }
        }
    }

    /**
     * 將訊息從redis中移除，比對user和ip
     * @param userId
     * @param clientIp
     */
    public void deleteObjectByUserAndIp(Long userId, String clientIp){
        Set<String> tokens = redisTemplate.keys("*");
        for(String token : tokens) {
            Object userIdValue = redisTemplate.opsForHash().get(token, "userId");
            Object clientIpValue = redisTemplate.opsForHash().get(token, "clientIp");
            if(userIdValue!=null && clientIpValue!=null && userIdValue.equals(userId.toString()) && clientIpValue.equals(clientIp)) {
                redisTemplate.opsForHash().delete(token, "userId");
            }
        }
    }
}
