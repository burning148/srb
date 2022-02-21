package com.atguigu.common.exception;

import com.atguigu.common.result.ResponseEnum;
import lombok.Data;

/**
 * @Author wangjun
 * @Date 2021/9/13 17:15
 * @Description
 */
@Data
public class BusinessException extends RuntimeException{
    private Integer code;
    private String message;

    public BusinessException(String message) {
        this.message = message;
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(cause);
        this.message = message;
        this.code = code;
    }

    public BusinessException(ResponseEnum responseEnum) {
        this.message = responseEnum.getMessage();
        this.code = responseEnum.getCode();
    }

    public BusinessException(ResponseEnum responseEnum, Throwable cause) {
        super(cause);
        this.message = responseEnum.getMessage();
        this.code = responseEnum.getCode();
    }

}
