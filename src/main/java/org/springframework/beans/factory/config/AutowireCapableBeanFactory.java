package org.springframework.beans.factory.config;

import org.springframework.beans.BeanException;
import org.springframework.beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 执行BeanPostProcessors的postProcessBeforeInitialization方法
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeanException;

    /**
     * 执行BeanPostProcessors的postProcessAfterInitialization方法
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeanException;

}