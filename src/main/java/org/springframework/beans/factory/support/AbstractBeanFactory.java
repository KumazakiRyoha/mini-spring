package org.springframework.beans.factory.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final Map<String, Object> singletonObjectCache = new HashMap<>();

    @Override
    public Object getBean(String name) {
        Object shareInstance = getSingleton(name);
        if (shareInstance != null) {
            // 处理单例
            return getObjectForBeanInstance(shareInstance, name);
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);
        Object bean = createBean(name, beanDefinition);
        return getObjectForBeanInstance(bean, name);
    }

    /**
     * 处理 FactoryBean，返回其创建的对象
     */
    protected Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        Object object = beanInstance;
        if (beanInstance instanceof FactoryBean) {
            FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
            try {
                if (factoryBean.isSingleton()) {
                    // 从缓存中获取
                    object = this.singletonObjectCache.get(beanName);
                    if (object == null) {
                        object = factoryBean.getObject();
                        this.singletonObjectCache.put(beanName, object);
                    }
                } else {
                    // 非单例每次创建新的对象
                    object = factoryBean.getObject();
                }

            } catch (Exception e) {
                throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", e);
            }
        }
        return object;

    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeanException {
        return requiredType.cast(getBean(name));
    }

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeanException;

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeanException;

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        // 有则覆盖
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

}
