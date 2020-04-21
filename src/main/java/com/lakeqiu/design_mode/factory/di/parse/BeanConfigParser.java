package com.lakeqiu.design_mode.factory.di.parse;

import com.lakeqiu.design_mode.factory.di.BeanDefinition;

import java.io.InputStream;
import java.util.List;

/**
 * @author lakeqiu
 */
public interface BeanConfigParser {
    /**
     * 读取配置文件流中Bean定义返回BeanDefinition集合
     * @param in
     * @return
     */
    List<BeanDefinition> parse(InputStream in);

    /**
     * 读取配置文件中Bean定义返回BeanDefinition集合
     * @param configContent
     * @return
     */
    List<BeanDefinition> parse(String configContent);
}
