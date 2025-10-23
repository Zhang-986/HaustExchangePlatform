package com.haust.forum.annotation;

import com.haust.common.domain.enumeration.ContentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 注解可以用于方法
@Retention(RetentionPolicy.RUNTIME) // 注解在运行时生效
public @interface SensitiveMonitor {
    ContentType value();
}
