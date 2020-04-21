package com.lakeqiu.design_mode.factory.di;

/**
 * @author lakeqiu
 */
public interface ApplicationContext {
    /**
     * 获取bean
     * @param beanId
     * @return
     */
    Object getBean(String beanId);
}
