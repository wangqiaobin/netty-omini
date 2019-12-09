package com.wei.omini.exception;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-09-12 13:52
 */
public class NotFoundRegistryException extends RuntimeException {
    public NotFoundRegistryException() {
    }

    public NotFoundRegistryException(String message) {
        super(message);
    }

    public NotFoundRegistryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundRegistryException(Throwable cause) {
        super(cause);
    }

    public NotFoundRegistryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
