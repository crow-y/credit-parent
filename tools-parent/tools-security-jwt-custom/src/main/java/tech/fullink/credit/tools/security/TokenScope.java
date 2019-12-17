package tech.fullink.credit.tools.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * token 校验注解
 * @author Jamestang on 2017/11/4.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenScope {
    TokenType[] value() default {TokenType.USER};
}
