package org.springframework.beans.factory.config;

import org.springframework.beans.BeanException;

/**
 * 用于修改实例化bean之前和之后的bean属性值
 */

public interface BeanPostProcessor {

    /**
     * bean执行初始化方法前调用
     * 
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeanException;

    /**
     * bean执行初始化方法后调用
     * 
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeanException;

}
