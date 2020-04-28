package com.fc.work.handler;

import com.fc.work.exception.LoginException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理公共异常处理机制
 * @author fangyuan
 */
@RestControllerAdvice
public class LoginExceptionHandler {


    /**
     * 处理自定义异常 返回指定信息
     * @param re
     * @return
     */
    @ExceptionHandler({LoginException.class})
    public Map<String,Object> handleNoAutherException(final Exception re) {
        re.printStackTrace();
        Map<String,Object> rs = new HashMap();
        rs.put("success",false);
        rs.put("errorMessage",re.getMessage());
        return rs;
    }



}
