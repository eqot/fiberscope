package com.eqot.fiberscope.processor;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@Target(TYPE)
public @interface Fiberscope {
    Class<?> value() default Object.class;
    String[] methods() default {};
}
