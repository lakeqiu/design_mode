package com.lakeqiu.design_mode.factory.di;

import com.lakeqiu.design_mode.factory.di.exception.NoSuchBeanDefinitionException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lakeqiu
 */
public class BeansFactory {

    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();

    void addBeanDefinitions(List<BeanDefinition> beanDefinitions) {
        // 将BeanDefinition加入容器中
        for (BeanDefinition beanDefinition : beanDefinitions) {
            this.beanDefinitions.putIfAbsent(beanDefinition.getId(), beanDefinition);
        }

        // 将不是懒加载和单例的先创建出来
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (beanDefinition.isLazyInit() && beanDefinition.isSingleton()) {
                creatBean(beanDefinition);
            }
        }
    }

    public Object getBean(String beanId) {
        BeanDefinition beanDefinition = beanDefinitions.get(beanId);
        if (beanDefinition == null) {
            throw new NoSuchBeanDefinitionException("没有找到bean" + beanId);
        }
        return creatBean(beanDefinition);
    }

    protected Object creatBean(BeanDefinition beanDefinition) {
        // 如果BeanDefinition是单例并且单例容器中存在，直接从单例容器中获取返回
        if (beanDefinition.isSingleton() && singletonObjects.containsKey(beanDefinition.getId())) {
            return singletonObjects.get(beanDefinition.getId());
        }

        Object bean = null;
        try {
            // 反射获取
            Class beanClass = Class.forName(beanDefinition.getClassName());
            List<BeanDefinition.ConstructorArg> args = beanDefinition.getConstructorArgs();

            // bean没有懒加载、依赖等其他信息，直接创建即可
            if (args.isEmpty()) {
                bean = beanClass.newInstance();
            } else {
                //
                Class[] argClasses = new Class[args.size()];
                Object[] argObjects = new Object[args.size()];
                for (int i = 0; i < args.size(); i++) {
                    BeanDefinition.ConstructorArg arg = args.get(i);
                    // 当前bean没有依赖其他bean
                    if (!arg.isRef()) {
                        argClasses[i] = arg.getType();
                        argObjects[i] = arg.getArg();
                    } else {
                        BeanDefinition refBeanDefinition = beanDefinitions.get(arg.getArg());
                        if (refBeanDefinition == null) {
                            throw new NoSuchBeanDefinitionException(arg.getArg() + "没有找到");
                        }

                        // 先创建依赖的bean
                        argClasses[i] = Class.forName(refBeanDefinition.getClassName());
                        creatBean(refBeanDefinition);
                    }
                }
                bean = beanClass.getConstructor(argClasses).newInstance(argObjects);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 创建出bean并且bean是单例的
        if (bean != null && beanDefinition.isSingleton()) {
            // 加入单例容器中
            singletonObjects.putIfAbsent(beanDefinition.getId(), bean);
            return singletonObjects.get(beanDefinition.getId());
        }

        return bean;
    }
}
