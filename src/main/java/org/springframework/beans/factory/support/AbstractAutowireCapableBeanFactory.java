package org.springframework.beans.factory.support;

import org.springframework.beans.BeanException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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
            // 为bean填充属性
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
     * 
     * @param beanDefinition
     * @param beanName
     * @return
     */
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();

                if (value instanceof BeanReference) {
                    // beanA依赖beanB，先实例化beanB
                    // 处理BeanReference类型
                    BeanReference beanReference = (BeanReference) value;
                    // 获取引用的bean实例
                    value = getBean(beanReference.getBeanName());

                }

                // 使用通用的设置字段值方法
                setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            throw new BeanException("Error setting property values for bean: " + beanName, e);
        }
    }

    /**
     * 设置字段值，支持多种类型
     * 
     * @param bean      Bean对象
     * @param fieldName 字段名
     * @param value     字段值
     */

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void setFieldValue(Object bean, String fieldName, Object value) {
        if (bean == null || fieldName == null) {
            return;
        }

        // 根据对象类型分别处理
        if (bean instanceof Map) {
            // 处理Map类型
            ((Map) bean).put(fieldName, value);
        } else if (bean instanceof List) {
            // 处理List类型，尝试将fieldName转换为整数索引
            try {
                int index = Integer.parseInt(fieldName);
                List list = (List) bean;
                // 确保List大小足够
                while (list.size() <= index) {
                    list.add(null);
                }
                list.set(index, value);
            } catch (NumberFormatException e) {
                throw new BeanException("Error setting property to List: " + fieldName + " is not a valid index", e);
            }
        } else {
            // 处理普通Java Bean
            try {
                // 1. 首先尝试setter方法
                String setterMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                // 尝试获取setter方法
                Method setterMethod = findSetterMethod(bean.getClass(), setterMethodName, value);
                if (setterMethod != null) {
                    // 使用setter方法设置值
                    setterMethod.setAccessible(true);
                    setterMethod.invoke(bean, value);
                    return;
                }

                // 2. 如果没有找到setter方法，尝试直接设置字段
                java.lang.reflect.Field field = findField(bean.getClass(), fieldName);
                if (field != null) {
                    field.setAccessible(true);
                    field.set(bean, value);
                } else {
                    throw new BeanException("No such field: " + fieldName + " in class " + bean.getClass().getName());
                }
            } catch (Exception e) {
                throw new BeanException("Error setting property " + fieldName, e);
            }
        }
    }

    /**
     * 查找合适的setter方法
     */
    private Method findSetterMethod(Class<?> beanClass, String setterMethodName, Object value) {
        // 先直接查找精确匹配的setter方法
        if (value != null) {
            try {
                return beanClass.getMethod(setterMethodName, value.getClass());
            } catch (NoSuchMethodException e) {
                // 继续尝试其他可能的方法
            }
        }

        // 查找所有可能的setter方法
        Method[] methods = beanClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(setterMethodName) && method.getParameterCount() == 1) {
                Class<?> paramType = method.getParameterTypes()[0];
                // 检查参数类型兼容性
                if (value == null || paramType.isInstance(value)) {
                    return method;
                }
            }
        }

        return null;
    }

    /**
     * 查找字段，支持向上查找继承层次
     */
    private java.lang.reflect.Field findField(Class<?> beanClass, String fieldName) {
        Class<?> searchType = beanClass;
        while (searchType != null && searchType != Object.class) {
            try {
                return searchType.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                searchType = searchType.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 使用实例化策略创建bean实例
     * 
     * @param instantiationStrategy
     */
    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName) {
        return getInstantiationStrategy().instantiate(beanDefinition, beanName, this);
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}