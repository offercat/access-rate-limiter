package com.github.offercat.access.rate.limiter.core.exception;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年09月02日 19:14:19
 */
public class MethodInvokeException extends RuntimeException {

    public MethodInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodInvokeException(Throwable cause) {
        super(cause);
    }

    public MethodInvokeException(String message) {
        super(message);
    }
}
