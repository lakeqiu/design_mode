package com.lakeqiu.design_mode.factory.di;

import com.lakeqiu.design_mode.factory.di.parse.BeanConfigParser;
import com.lakeqiu.design_mode.factory.di.parse.XmlBeanConfigParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author lakeqiu
 */
public class ClassXmlApplicationContext implements ApplicationContext {
    /**
     * bean工厂
     */
    private BeansFactory beansFactory;
    /**
     * bean解析器
     */
    private BeanConfigParser beanConfigParser;

    public ClassXmlApplicationContext(String configLocation) {
        this.beansFactory = new BeansFactory();
        this.beanConfigParser = new XmlBeanConfigParser();
        loadBeanDefinitions(configLocation);
    }

    /**
     * 读取bean定义
     * @param configLocation
     */
    private void loadBeanDefinitions(String configLocation) {
        InputStream in = null;
        try {
            // 获取工程根目录+文件（即根目录下的文件流）
            in = this.getClass().getResourceAsStream("/" + configLocation);
            if (in == null) {
                throw new RuntimeException("不能找到配置文件" + configLocation);
            }
            // 读取配置文件中所有bean并转化为BeanDefinition
            List<BeanDefinition> beanDefinitions = beanConfigParser.parse(in);
            // 加入工厂
            beansFactory.addBeanDefinitions(beanDefinitions);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object getBean(String beanId) {
        return null;
    }
}
