package org.springframework.beans.factory.support;

import org.springframework.beans.BeanException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.BeanReference;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
        implements AutowireCapableBeanFactory {

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

            // 执行bean的初始化方法和BeanPostProcessor的前置和后置处理方法
            initializeBean(beanName, bean, beanDefinition);

        } catch (Exception e) {
            throw new BeanException("Instantiation of bean failed", e);
        }

        // 注册有销毁方法的bean
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        // 注册单例
        registerSingleton(beanName, bean);
        return bean;
    }

    /**
     * 注册有销毁方法的bean，即bean继承自DisposableBean或有自定义的销毁方法
     * 
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        // 如果bean实现了DisposableBean接口，注册销毁方法
        if (bean instanceof DisposableBean) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }

        // 如果有自定义的销毁方法，注册销毁方法
        String destroyMethodName = beanDefinition.getDestroyMethodName();
        if (destroyMethodName != null && !destroyMethodName.isEmpty()) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
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

        if (bean instanceof Map) {
            ((Map) bean).put(fieldName, value);
        } else if (bean instanceof List) {
            ((List) bean).add(value);
        } else {
            try {
                // 首先尝试setter方法
                String setterMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                try {
                    Method setter = bean.getClass().getMethod(setterMethodName, value.getClass());
                    setter.invoke(bean, value);
                    return;
                } catch (NoSuchMethodException e) {
                    // 如果没有找到完全匹配的setter，尝试查找其他可能的setter
                    Method[] methods = bean.getClass().getMethods();
                    for (Method method : methods) {
                        if (method.getName().equals(setterMethodName) && method.getParameterCount() == 1) {
                            // 找到一个潜在的setter，尝试类型转换
                            Class<?> paramType = method.getParameterTypes()[0];
                            Object convertedValue = convertValueToType(value, paramType);
                            if (convertedValue != null) {
                                method.invoke(bean, convertedValue);
                                return;
                            }
                        }
                    }
                }

                // 如果没有找到合适的setter，尝试直接设置字段
                java.lang.reflect.Field field = bean.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                // 尝试进行类型转换
                Object convertedValue = convertValueToType(value, field.getType());
                if (convertedValue != null) {
                    field.set(bean, convertedValue);
                } else {
                    // 如果转换失败但值不为null，尝试直接设置（可能会抛出异常）
                    if (value != null) {
                        field.set(bean, value);
                    }
                }
            } catch (Exception e) {
                throw new BeanException("Error setting property " + fieldName, e);
            }
        }
    }

    /**
     * 将值转换为目标类型
     * 
     * @param value      要转换的值
     * @param targetType 目标类型
     * @return 转换后的值，如果无法转换则返回null
     */
    private Object convertValueToType(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        // 如果值已经是目标类型，直接返回
        if (targetType.isInstance(value)) {
            return value;
        }

        // String到基本类型的转换
        if (value instanceof String) {
            String strValue = (String) value;

            if (targetType == int.class || targetType == Integer.class) {
                return Integer.parseInt(strValue);
            } else if (targetType == long.class || targetType == Long.class) {
                return Long.parseLong(strValue);
            } else if (targetType == double.class || targetType == Double.class) {
                return Double.parseDouble(strValue);
            } else if (targetType == float.class || targetType == Float.class) {
                return Float.parseFloat(strValue);
            } else if (targetType == boolean.class || targetType == Boolean.class) {
                return Boolean.parseBoolean(strValue);
            } else if (targetType == short.class || targetType == Short.class) {
                return Short.parseShort(strValue);
            } else if (targetType == byte.class || targetType == Byte.class) {
                return Byte.parseByte(strValue);
            } else if (targetType == char.class || targetType == Character.class && strValue.length() == 1) {
                return strValue.charAt(0);
            }
        }

        // 其他类型转换逻辑可以根据需要添加

        return null; // 无法转换
    }

    /**
     * 查找合适的setter方法
     */
    @SuppressWarnings("unused")
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
     * 
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
     * 
     * 执行bean的初始化方法和BeanPostProcessor的前置和后置处理方法
     * 
     * @param beanDefinition
     * @param beanName
     * @return
     * @throws Exception
     */
    protected Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        // 执行BeanPostProcessor的前置处理方法
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        // 执行bean的初始化方法
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition); // 第二次调用，这里似乎是重复的
        } catch (Throwable e) {
            throw new BeanException("Invocation of init method failed for bean '" + beanName + "'", e);
        }

        // 执行BeanPostProcessor的后置处理方法
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);

        return wrappedBean;
    }

    /**
     * 执行BeanPostProcessor的后置处理方法
     * 
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName) throws BeanException {

        Object result = bean;

        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;

    }

    /**
     * 执行BeanPostProcessor的后置处理方法
     * 
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object bean, String beanName) throws BeanException {

        Object result = bean;

        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;

    }

    /**
     * 执行bean的初始化方法
     * 
     * @param beanDefinition
     * @param beanName
     * @return
     * @throws BeanException if an error occurs during initialization
     */
    protected void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        if (bean instanceof InitializingBean) {
            // 如果bean实现了InitializingBean接口，调用afterPropertiesSet方法
            ((InitializingBean) bean).afterPropertiesSet();
        }
        String initMethodName = beanDefinition.getInitMethodName();
        if (initMethodName != null && !initMethodName.isEmpty()) {
            // 如果有自定义的初始化方法，使用反射调用
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            // 检查方法是否存在
            if (initMethod == null) {
                throw new BeanException("No such init method: " + initMethodName + " for bean: " + beanName);
            }
            initMethod.invoke(bean);

        }
    }

    /**
     * 
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