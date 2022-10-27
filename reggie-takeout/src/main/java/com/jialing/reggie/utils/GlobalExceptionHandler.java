package com.jialing.reggie.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局底层异常chuliq
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})       // 代理模式
@ResponseBody       // 最终就将结果封装成json数据返回
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 一旦ControllerAdvice指定的Controller出现ExceptionHandler注解中的异常
     * 就会被下面方法捕获并处理
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        return R.error("失败了");
    }
}
