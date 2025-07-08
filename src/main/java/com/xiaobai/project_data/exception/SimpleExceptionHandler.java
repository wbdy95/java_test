package com.xiaobai.project_data.exception;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SimpleExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        JSONObject object = new JSONObject();
        object.put("msg", errorMessage);
        object.put("success", false);
        object.put("code", 400);
        object.put("time", System.currentTimeMillis());
        return object.toString();
    }
}