package com.hj.reggie.commons.exception;

/**
 * 自定义业务异常
 *
 * @create 2023-03-10 11:08
 */
public class CustomException extends RuntimeException{
    static final long serialVersionUID = -7034897166939L;
    public CustomException(){super();}
    public CustomException(String message){super(message);}
}
