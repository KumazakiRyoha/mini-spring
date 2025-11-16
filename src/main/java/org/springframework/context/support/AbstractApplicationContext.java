package org.springframework.context.support;

import java.util.Map;

import org.springframework.beans.BeanException;
import org.springframework.beans.factory.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationEvent;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.io.DefaultResourceLoader;

public abstract class AbstractApplicationContext extends DefaultResourceLoader
        implements ConfigurableApplicationContext {

    // 事件广播器的名称
    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    // 事件广播器实例
    private ApplicationEventMulticaster applicationEventMulticaster;

    @Override
    public void refresh() {
        // 创建BeanFactory，并加载BeanDefinition
        refreshBeanFactory();

        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 设置ApplicationContext到BeanFactory中
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        // 在bean实例化之前，执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        // BeanPostProcessor需要提前与其他bean对象实例化之前注册
        registerBeanPostProcessors(beanFactory);

        // 初始化事件广播器
        initApplicationEventMulticaster();

        // 注册事件监听器
        registerListeners();

        // 提前实例化单例bean对象
        beanFactory.preInstantiateSingletons();
    }

    /**
     * 初始化事件广播器
     */
    protected void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
    }

    /**
     * 注册事件监听器
     */
    protected void registerListeners() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        Map<String, ApplicationListener> applicationListenerMap = beanFactory
                .getBeansOfType(ApplicationListener.class);
        for (ApplicationListener applicationListener : applicationListenerMap.values()) {
            this.applicationEventMulticaster.addApplicationListener(applicationListener);
        }
    }

    /**
     * 发布容器刷新完成事件
     * 
     * @throws BeanException
     */
    protected void publishRefreshEvent() throws BeanException {
        publishEvent(new ContextRefreshedEvent(this));
    }

    /**
     * 实现事件发布接口
     * 
     * @throws BeanException
     */
    @Override
    public void publishEvent(ApplicationEvent event) throws BeanException {
        applicationEventMulticaster.multicastEvent(event);
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

        // 发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));

        destroyBeans();
    }

    /**
     * 销毁所有单例bean
     */
    protected void destroyBeans() {
        getBeanFactory().destroySingletons();
    }

}
