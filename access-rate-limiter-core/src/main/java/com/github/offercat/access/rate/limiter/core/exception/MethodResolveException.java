package com.github.offercat.access.rate.limiter.core.exception;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年09月03日 15:51:04
 */
public class MethodResolveException extends RuntimeException {

    public MethodResolveException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodResolveException(Throwable cause) {
        super(cause);
    }

    public MethodResolveException(String message) {
        super(message);
    }
}
