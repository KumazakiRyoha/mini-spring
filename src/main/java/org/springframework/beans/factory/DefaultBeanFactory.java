package org.springframework.beans.factory;

import org.springframework.beans.BeanException;
import org.springframework.beans.factory.config.BeanDefinition;

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
                bean = beanDefinition.getBeanClass().newInstance();
                singletonObjects.put(name, bean);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new BeanException("Failed to instantiate bean named '" + name + "'", e);
            }
        }
        return bean;
    }

    public void registerBean(String name, Object bean) {
        singletonObjects.put(name, bean);
        beanDefinitionMap.put(name, new BeanDefinition(bean.getClass()));
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeanException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBean'");
    }
}
