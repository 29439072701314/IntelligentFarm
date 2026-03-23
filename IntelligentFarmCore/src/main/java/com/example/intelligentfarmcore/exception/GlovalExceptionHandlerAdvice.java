package com.example.intelligentfarmcore.exception;

import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlovalExceptionHandlerAdvice {

    Logger log = LoggerFactory.getLogger(GlovalExceptionHandlerAdvice.class);
    @ExceptionHandler({Exception.class})  // 什么异常的统一处理
    public ResponseMessage<?> handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        // 记录异常信息
        log.error("统一异常处理：", e);
        return new ResponseMessage<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
    }
}
