package org.springframework.beans.factory.support;

import org.springframework.beans.BeanException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class DefaultBeanFactory implements BeanFactory {

    private final Map<String, Object> singletonObjects = new HashMap<>();
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    public Object getBean(String name) throws BeanException {
        Object bean = singletonObjects.get(name);
        if (bean == null) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(name);
            if (beanDefinition == null) {
                throw new BeanException("No bean named '" + name + "' is defined");
            }
            try {
                Constructor<?> constructor = beanDefinition.getBeanClass().getDeclaredConstructor();
                constructor.setAccessible(true); // 处理私有构造函数的情况
                bean = constructor.newInstance();
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                    | InvocationTargetException e) {
                throw new BeanException("实例化Bean失败: " + beanDefinition.getBeanClass().getName(), e);
            }
            singletonObjects.put(name, bean);
        }
        return bean;
    }

    public void registerBean(String name, Object bean) {
        singletonObjects.put(name, bean);
        beanDefinitionMap.put(name, new BeanDefinition(bean.getClass()));
    }
}