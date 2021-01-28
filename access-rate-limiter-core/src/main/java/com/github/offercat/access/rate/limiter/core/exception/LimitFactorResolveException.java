package com.github.offercat.access.rate.limiter.core.exception;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年09月01日 21:32:44
 */
public class LimitFactorResolveException extends RuntimeException {

    public LimitFactorResolveException(String message, Throwable cause) {
        super(message, cause);
    }

    public LimitFactorResolveException(Throwable cause) {
        super(cause);
    }

    public LimitFactorResolveException(String message) {
        super(message);
    }
}
