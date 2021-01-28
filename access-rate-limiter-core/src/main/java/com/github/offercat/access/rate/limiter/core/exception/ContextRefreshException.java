package com.github.offercat.access.rate.limiter.core.exception;

/**
 * 上下文刷新异常
 *
 * @author 徐通 xutong34
 * @since 2020年09月04日 16:04:08
 */
public class ContextRefreshException extends RuntimeException {

    public ContextRefreshException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContextRefreshException(Throwable cause) {
        super(cause);
    }

    public ContextRefreshException(String message) {
        super(message);
    }
}
