package com.arzio.arziolib.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterModule {
    public String name();
    public Class<? extends ModuleContainer> container() default ModuleContainer.class;
    public boolean defaultState() default true;
}
