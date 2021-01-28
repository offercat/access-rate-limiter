package com.github.offercat.access.rate.limiter.core.util;

import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;

/**
 * Description
 *
 * @author 徐通 xutong34
 * @since 2020年09月02日 21:35:29
 */
public class TimeUtil {

    public static long calculateMilliseconds(int time, TimeUnit timeUnit) {
        long result = -1;
        if (timeUnit == TimeUnit.MILLISECONDS) {
            result = time;
        } else if (timeUnit == TimeUnit.SECONDS) {
            result = time * 1000;
        } else if (timeUnit == TimeUnit.MINUTES) {
            result = time * 1000 * 60;
        } else if (timeUnit == TimeUnit.HOURS) {
            result = time * 1000 * 60 * 60;
        } else if (timeUnit == TimeUnit.DAYS) {
            result = time * 1000 * 60 * 60 * 24;
        }
        if (result == -1 || time < 0) {
            throw new InvalidParameterException("不支持的时间单位类型 time = " + time + " timeUnit = " + timeUnit);
        }
        return result;
    }
}
