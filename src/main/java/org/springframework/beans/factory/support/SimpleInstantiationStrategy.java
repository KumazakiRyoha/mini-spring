package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;

public class SimpleInstantiationStrategy implements InstantiationStrategy {

    /**
     * 简单的bean实例化策略，根据bean的无参构造函数实例化对象
     * 
     * @param beanDefinition
     * @return
     * @throws Exception
     */
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, BeanFactory owner) throws BeansException {
        Class<?> beanClass = beanDefinition.getBeanClass();
        try {
            return beanClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new BeansException("Failed to instantiate [" + beanClass.getName() + "]", e);
        }
    }
}
