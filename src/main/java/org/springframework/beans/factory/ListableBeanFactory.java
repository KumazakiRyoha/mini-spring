package org.springframework.beans.factory;

import java.util.Map;

import org.springframework.beans.BeanException;

public interface ListableBeanFactory extends BeanFactory {

    /**
     * 返回指定类型的所有实例
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeanException;

    /**
     * 返回所有bean的名称
     */
    String[] getBeanDefinitionNames();
}
