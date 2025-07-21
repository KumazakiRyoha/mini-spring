package org.springframework.context.support;

import java.util.Map;

import org.springframework.beans.BeanException;
import org.springframework.beans.factory.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;

public abstract class AbstractApplicationContext extends DefaultResourceLoader
        implements ConfigurableApplicationContext {

    @Override
    public void refresh() {
        // 创建BeanFactory，并加载BeanDefinition
        refreshBeanFactory();

        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 在bean实例化之前，执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        // BeanPostProcessor需要提前与其他bean对象实例化之前注册
        registerBeanPostProcessors(beanFactory);

        // 提前实例化单例bean对象
        beanFactory.preInstantiateSingletons();
    }

    /**
     * 获取BeanFactory, 并加载BeanDefinition
     * 
     * @return
     * @throws BeanException
     */
    protected abstract void refreshBeanFactory() throws BeanException;

    /**
     * 在bean实例化之前，执行BeanFactoryPostProcessor
     * 
     * @param beanFactory
     */
    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory
                .getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    /**
     * 注册BeanPostProcessor
     * 
     * @param beanFactory
     */
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory
                .getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanFactoryPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanFactoryPostProcessor);
        }
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredTypeClass) throws BeanException {
        return getBeanFactory().getBean(name, requiredTypeClass);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeanException {
        return getBeanFactory().getBeansOfType(type);
    }

    public Object getBean(String name) throws BeanException {
        return getBeanFactory().getBean(name);
    }

    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    public abstract ConfigurableListableBeanFactory getBeanFactory();

<<<<<<< HEAD
    public void close() {
        doClose();
    }

    public void registerShutdownHook() {
        Thread shutdownHook = new Thread() {
            @Override
            public void run() {
                doClose();
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    protected void doClose() {
        destroyBeans();
    }

    /**
     * 销毁所有单例bean
     */
    protected void destroyBeans() {
        getBeanFactory().destroySingletons();
    }

=======
>>>>>>> ed89fb3c83e68a6f523c61be323fada2202ec6fa
}
