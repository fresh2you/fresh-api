package com.zb.fresh_api.domain.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Reader {

    @AliasFor(annotation = Component.class)
    String value() default "";

}
