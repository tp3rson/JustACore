package ru.justagod.justacore.initialization.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by JustAGod on 10.12.17.
 */
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RegistryObject {

    @SuppressWarnings("unused")
    String[] dependencies() default {};

    @SuppressWarnings("unused")
    String registryId() default  "";

    @SuppressWarnings("unused")
    boolean customRegistry() default false;

    /**
     * Только для блоков!
     * @return айтем блок.
     */
    @SuppressWarnings("unused")
    String itemBlock() default  "";
}