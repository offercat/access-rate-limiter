package com.github.offercat.access.rate.limiter.core.resolver;

import com.github.offercat.access.rate.limiter.core.annotated.LimitFactor;
import com.github.offercat.access.rate.limiter.core.LimitFactorFunction;
import com.github.offercat.access.rate.limiter.core.exception.LimitFactorResolveException;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.logging.Logger;

/**
 * 默认限制因子解析器
 *
 * @author 徐通 xutong34
 * @since 2020年09月01日 21:39:43
 */
public class DefaultLimitFactorResolver implements LimitFactorResolver {

    private static final Logger log = Logger.getLogger(DefaultLimitFactorResolver.class.getName());

    @Override
    public List<String> resolve(Parameter[] parameters, Object[] args, LimitFactorFunction[] limitFactorFunctions, Object target) {
        if (parameters.length != args.length) {
            log.warning("resolve | parameters 与 args 长度不匹配，放弃解析");
            return Collections.emptyList();
        }
        List<String> limitFlags = new ArrayList<>();
        for (int i=0; i<parameters.length; i++) {
            LimitFactor limitFactor = parameters[i].getAnnotation(LimitFactor.class);
            if (limitFactor != null) {
                limitFlags.add(this.resolveFactor(limitFactor.value(), args[i], i));
            }
        }
        if (limitFactorFunctions != null) {
            for (int i=0; i<limitFactorFunctions.length; i++) {
                if (limitFactorFunctions[i] == null) {
                    continue;
                }
                limitFlags.add("[method#" + i + ":(" + limitFactorFunctions[i].getStrFactor(target) + ")]");
            }
        }
        return limitFlags;
    }

    private String resolveFactor(String[] expressions, Object arg, int index) {
        if (expressions.length == 0) {
            return "[index#" + index + ":(" + arg.toString() + ")]";
        }
        if (expressions.length == 1 && "".equals(expressions[0])) {
            return "[index#" + index + ":(" + arg.toString() + ")]";
        }
        StringBuilder factor = new StringBuilder("[index#" + index + ":(");
        try {
            for (int i=0; i<expressions.length; i++) {
                String[] fieldNameArray = expressions[i].split("\\.");
                Object currentField = arg;
                for (String fieldName : fieldNameArray) {
                    Field field = currentField.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    currentField = field.get(currentField);
                }
                if (currentField != null) {
                    if (i == 0) {
                        factor.append(expressions[i]).append("->").append(currentField.toString());
                    } else {
                        factor.append(", ").append(expressions[i]).append("->").append(currentField.toString());
                    }
                }
            }
            factor.append(")]");
        } catch (Throwable e) {
            throw new LimitFactorResolveException("限制因子表达式解析错误 expressions = " + Arrays.toString(expressions), e);
        }
        return factor.toString();
    }
}
