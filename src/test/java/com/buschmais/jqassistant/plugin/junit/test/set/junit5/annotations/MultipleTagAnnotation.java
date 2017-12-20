package com.buschmais.jqassistant.plugin.junit.test.set.junit5.annotations;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Tag("fast")
@Tag("slow")
@Tag("light")
public @interface MultipleTagAnnotation {
}