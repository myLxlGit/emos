package com.example.emos.wx.config;

import com.example.emos.wx.common.util.base.dto.constant.ValidateError;
import com.example.emos.wx.common.util.base.dto.core.ResultT;
import com.example.emos.wx.exception.EmosException;
import com.github.lianjiatech.retrofit.spring.boot.exception.RetrofitException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局捕获异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionProcessor {

    //申明捕获那个异常类
    @ExceptionHandler({EmosException.class, Exception.class})
    public ResultT<Void> exceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
        String message = ex.getMessage();
        return ResultT.error(message);
//        if (ex instanceof MethodArgumentNotValidException) {
//            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
//            return ResultT.error(exception.getBindingResult().getFieldError().getDefaultMessage());
//        } else if (ex instanceof EmosException) {
//            EmosException exception = (EmosException) ex;
//            return ResultT.error(exception.getMsg());
//
//        } else if (ex instanceof UnauthorizedException) {
//            return ResultT.error("你不具备相关权限");
//        } else {
//            String message = ex.getMessage();
//            return ResultT.error(message);
//        }
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResultT<Void> exceptionHandler(UnauthorizedException ex) {
        log.error(ex.getMessage(), ex);
        return ResultT.error("你不具备相关权限");

    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResultT<List<ValidateError>> exceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<ValidateError> collect = bindingResult.getAllErrors().stream().map(ValidateError::new).collect(Collectors.toList());
        log.info(e.getMessage());
        return ResultT.error(HttpStatus.BAD_REQUEST.value(), "参数校验失败", collect);
    }


    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResultT<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResultT.error("请求转化失败");
    }

    @ExceptionHandler({SQLException.class})
    public ResultT<Void> handleSqlException(SQLException ex) {
        if (ex.getErrorCode() == 12899 || ex.getErrorCode() == 1461) {
            log.error(ex.getMessage(), ex);
            return ResultT.error("字段长度过长");
        }
        log.error(ex.getMessage(), ex);
        return ResultT.error("操作失败");
    }

    /**
     * retrofit 发送请求出现的错误
     *
     * @param ex 异常
     */
    @ExceptionHandler({RetrofitException.class})
    public ResultT<?> handleRetrofitException(RetrofitException ex) {
        log.error("发送远程请求出现错误", ex);
        return ResultT.error("远程服务请求失败", ex.getMessage());
    }

}
