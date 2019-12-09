package com.wei.omini.exception;

/**
 * @author qiaobinwang@qq.com
 * @version 1.0.0
 * @date 2019-11-18 16:59
 */
public class DuplicateRemoteException extends RuntimeException {
    public DuplicateRemoteException() {
    }

    public DuplicateRemoteException(String message) {
        super(message);
    }

    public DuplicateRemoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateRemoteException(Throwable cause) {
        super(cause);
    }

    public DuplicateRemoteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
