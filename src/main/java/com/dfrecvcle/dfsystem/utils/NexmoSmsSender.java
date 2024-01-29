package com.dfrecvcle.dfsystem.utils;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class NexmoSmsSender {

    private final static String APP_KEY = "g9tBhCjK";
    private final static String SECRET_KEY = "hCUSl5mV";

    /**
     *
     * @param phone
     * @param code
     * @param type 0: 註冊 1: 忘記密碼
     */
    public void SendSms(String phone, int code, int type) {
        try {
            // 建立URL對象
            URL url = new URL("http://api2.nxcloud.com/api/sms/mtsend");

            // 建立連接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // 設定POST請求的標頭
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", "Basic "
                    + Base64.getEncoder().encodeToString((APP_KEY + ":" + SECRET_KEY).getBytes(StandardCharsets.UTF_8)));

            // 84866746700
            // 84784444243
            // 84786174410
            String to = phone; //886988588547 886966645670 84392833602
            String text = "會員註冊驗證碼「" + code + "」，10分鐘有效。";
            if(type == 1) {
                text = "會員忘記密碼驗證碼「" + code + "」。";
            }

            // 建立請求參數
            String postData = "appkey="+ APP_KEY + "&secretkey=" + SECRET_KEY + "&phone=" + to + "&content=" + text;

            // 發送請求
            try (OutputStream os = connection.getOutputStream();
                 OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
                osw.write(postData);
                osw.flush();
            }

            // 建立一個BufferedReader來讀取HTTP響應
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                String jsonResponse = response.toString();
                System.out.println("回傳訊息：" + jsonResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }


            // 讀取響應
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("簡訊已成功發送！");
            } else {
                System.err.println("簡訊發送失敗，HTTP響應碼：" + responseCode);
            }

            connection.disconnect();

        } catch (Exception e) {
            System.err.println("牛信雲客戶端錯誤：" + e.getMessage());
        }
    }

    public static void main(String[] args) {

        NexmoSmsSender smsSender = new NexmoSmsSender();
        smsSender.SendSms("84784444243", 123456, 0);
    }
}

