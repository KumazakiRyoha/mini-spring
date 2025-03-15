package org.springframework.beans.factory.config;

import org.springframework.beans.BeanException;
import org.springframework.beans.factory.ConfigurableListableBeanFactory;

/**
 * 允许自定义修改应用程序上下文的bean定义，以及实例化bean之前的bean属性值的修改。
 */
public interface BeanFactoryPostProcessor {

    /**
     * 在标准初始化之后修改应用程序上下文的内部bean工厂。所有bean定义都将被加载，但是还没有bean被实例化。
     * 这允许覆盖或添加属性，甚至可以在实例化bean之前更改bean定义。
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeanException;

}
