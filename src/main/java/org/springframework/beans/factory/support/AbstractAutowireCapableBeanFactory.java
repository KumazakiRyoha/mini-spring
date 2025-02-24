package org.springframework.beans.factory.support;

import org.springframework.beans.BeanException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    // 定义实例化策略，默认使用SimpleInstantiationStrategy
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeanException {
        return doCreateBean(beanName, beanDefinition);
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = null;
        try {
            // 使用实例化策略创建bean实例
            bean = createBeanInstance(beanDefinition, beanName);
            //为bean填充属性
            applyPropertyValues(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeanException("Instantiation of bean failed", e);
        }

        // 注册单例
        registerSingleton(beanName, bean);
        return bean;
    }

    /**
     * 为bean填充属性
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    private void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {

        try {
            Class beanClass = beanDefinition.getBeanClass();
            for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
                String name = propertyValue.getName();
                String value = propertyValue.getValue();

                //通过属性的set方法设置属性
                Class<?> type = beanClass.getDeclaredField(name).getType();
                String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                Method method = beanClass.getDeclaredMethod(methodName, new Class[]{type});
                method.invoke(bean, new Object[]{value});

            }
        } catch (Exception e) {
            throw new BeanException("Error setting property values for bean: " + beanName, e);
        }




    }

    // 创建bean实例的方法
    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName) throws BeanException {
        return getInstantiationStrategy().instantiate(beanDefinition, beanName, this);
    }

    // getter和setter方法
    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}
