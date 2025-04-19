package org.springframework.context.support;

import org.springframework.beans.BeanException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    private DefaultListableBeanFactory beanFactory;

    /**
     * 创建BeanFactory，并加载BeanDefinition
     */
    @Override
    protected final void refreshBeanFactory() throws BeanException {
        // 创建BeanFactory
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        // 加载BeanDefinition
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    /**
     * 创建BeanFactory
     */
    protected DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    /**
     * 加载BeanDefinition
     */
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    public DefaultListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

}
