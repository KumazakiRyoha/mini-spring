package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;

/**
 * BeanDefinitionע���ӿ�
 */
public interface BeanDefinitionRegistry {

    /**
     * ��ע�����עBeanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
