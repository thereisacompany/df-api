package com.dfrecvcle.dfsystem.exception;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Slf4j
@Getter
public class BusinessRunTimeException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private int code;
    private Map<String, Object> data;

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//    private Object message = null;

    private JSONObject json = new JSONObject();

    public BusinessRunTimeException(String message) {
        this(HttpStatus.BAD_REQUEST, message);
    }

    public BusinessRunTimeException(HttpStatus httpStatus, String message) {
        json.put("message", "error");
        json.put("type", "fail");
        json.put("code", -1);

        this.httpStatus = httpStatus;
        json.put("result", message);
    }

    public BusinessRunTimeException(int code, String reason) {
//        super(reason);
        json.put("message", "error");
        json.put("type", "fail");
        json.put("code", code);

        httpStatus = HttpStatus.BAD_REQUEST;
        json.put("result", reason);

//        Map<String, Object> objectMap = new HashMap<>();
//        objectMap.put("message", reason);
//        this.code = code;
//        this.data = objectMap;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public JSONObject getJson() {
        return json;
    }

    public ResponseEntity<Object> generateResponseEntity() {

        ResponseEntity<Object> responseEntity = new ResponseEntity<Object>(json.toString(), httpStatus);
        return responseEntity;
    }

    @Override
    public String toString() {
        String data = json.toString();
        return "Http status:" + httpStatus + ". Error response:" + data;
    }
}
