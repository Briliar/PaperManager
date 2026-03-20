package com.grad.sys.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.error(500, "系统内部异常，请联系管理员");
    }
    
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        log.error("业务异常: ", e);
        return Result.error(500, e.getMessage());
    }
}
