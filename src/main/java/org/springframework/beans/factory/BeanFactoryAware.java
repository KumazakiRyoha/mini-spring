package org.springframework.beans.factory;

import org.springframework.beans.factory.support.BeansException;

public interface BeanFactoryAware extends Aware {
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}



