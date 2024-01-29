package com.dfrecvcle.dfsystem.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Tools {

    /**
     * 從Request對象中獲得客戶端IP，處理了HTTP代理服務器和Nginx的反向代理截取了ip
     *
     * @param request
     * @return ip
     */
    public static String getLocalIp(HttpServletRequest request) {
        String remoteAddr = getIpAddr(request);
        String forwarded = request.getHeader("X-Forwarded-For");
        String realIp = request.getHeader("X-Real-IP");

        String ip = null;
        if (realIp == null) {
            if (forwarded == null) {
                ip = remoteAddr;
            } else {
                ip = remoteAddr + "/" + forwarded.split(",")[0];
            }
        } else {
            if (realIp.equals(forwarded)) {
                ip = realIp;
            } else {
                if (forwarded != null) {
                    forwarded = forwarded.split(",")[0];
                }
                ip = realIp + "/" + forwarded;
            }
        }
        return ip;
    }

    /**
     * 獲取訪問者IP
     *
     * 在一般情況下使用Request.getRemoteAddr()即可，但是經過nginx等反向代理軟件後，這個方法會失效。
     *
     * 本方法先從Header中獲取X-Real-IP，如果不存在再從X-Forwarded-For獲得第一個IP(用,分割)，
     * 如果還不存在則調用Request .getRemoteAddr()。
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

    /**
     * 獲得當天時間，格式為yyyy-MM-dd HH:mm:ss
     * *
     * * @return 格式化後的日期格式
     */
    public static String getNow3() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String genConnectUrl(String key, String link, String push) {
        if(key == null) {
            key = "test123456aa";
        }
        if(link == null) {
            link = "/live/channel"; // or "/splive/channel"
        }
        int timeSet = 86400;
        String setPointLink = link;
        long currentTimestamp = System.currentTimeMillis() / 1000;
        long newTimestamp = currentTimestamp + timeSet;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTimeString = dateFormat.format(new Date(currentTimestamp * 1000));
        String dateTimeString2 = dateFormat.format(new Date(newTimestamp * 1000));

        System.out.println("Orginal Time: " + dateTimeString + "\n");
        System.out.println("Orginal Time (+" + timeSet + "s)/Expired: " + dateTimeString2 + "\n");
        System.out.println("TimeStamp (+" + timeSet + "s)/Expired: " + newTimestamp + "\n");
        System.out.println("HEX TimeStamp (+" + timeSet + "s)/Expired: " + Long.toHexString(newTimestamp) + "\n");
        System.out.println("--------------------------------\n");

        if(push == null) {
            push = "_default";
        }
        String hexTime = Long.toHexString(currentTimestamp);
        String URI = setPointLink + push;
        String MD5bundle = URI + key + hexTime;
        String result = getMD5Hash(MD5bundle);
        System.out.println("time: " + hexTime);
        System.out.println("URI: " + URI);
        System.out.println("Final: " + URI + "?wsSecret=" + result + "&wsTime=" + hexTime);
        System.out.println("--------------------------------\n");

        //rtmp://push.x-88.io
        String rtmp = "rtmp://push.x-88.io";
        return rtmp + URI + "?wsSecret=" + result + "&wsTime=" + hexTime;
    }

    private static String getMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成随机数字和字母组合
     * @param length
     * @return
     */
    public static String getCharAndNum(int length) {
        Random random = new Random();
        StringBuffer valSb = new StringBuffer();
        String charStr = "0123456789abcdefghijklmnopqrstuvwxyz";
        int charLength = charStr.length();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charLength);
            valSb.append(charStr.charAt(index));
        }
        return valSb.toString();
    }

    public static void main(String[] args) {
        //rtmp://push.x-88.io/live/channel16500?wsSecret=d09deaf2483a4c692f67f5d9c01c4094&wsTime=6554839a
        //rtmp://push.x-88.io/live/channel16500?wsSecret=8983b91c0c8a876c1641092dae00f27d&wsTime=65548ae8

        //rtmp://push.x-88.io/live/channel16483?wsSecret=2c7707cd9582984e510787fb9f350868&wsTime=65548873
        //rtmp://push.x-88.io/live/channel16483?wsSecret=5b5bdfae236476293d32654bbe73114f&wsTime=65548b2a
        System.out.println(">>>>"+Tools.genConnectUrl(null, null, "16483"));
    }
}
