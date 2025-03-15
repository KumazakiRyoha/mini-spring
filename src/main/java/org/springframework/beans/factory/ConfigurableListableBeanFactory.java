package org.springframework.beans.factory;

import org.springframework.beans.BeanException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * 提供配置和列举功能的BeanFactory
 */
public interface ConfigurableListableBeanFactory
        extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    /**
     * 根据名称获取BeanDefinition
     * 
     * @param beanName bean名称
     * @return BeanDefinition
     * @throws BeanException 如果找不到BeanDefinition
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeanException;

    /**
     * 提前实例化所有单例bean
     * 
     * @throws BeanException 如果实例化过程中出现异常
     */
    void preInstantiateSingletons() throws BeanException;

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
