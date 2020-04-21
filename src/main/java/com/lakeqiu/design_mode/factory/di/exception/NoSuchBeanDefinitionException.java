package com.lakeqiu.design_mode.factory.di.exception;

/**
 * @author lakeqiu
 */
public class NoSuchBeanDefinitionException extends RuntimeException {

    public NoSuchBeanDefinitionException(String message) {
        super(message);
    }
}
