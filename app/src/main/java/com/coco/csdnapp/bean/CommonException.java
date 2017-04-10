package com.coco.csdnapp.bean;

/**
 * 公共异常类.
 */

public class CommonException extends Exception {

    public CommonException() {
        super();
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(Throwable cause) {
        super(cause);
    }

}
