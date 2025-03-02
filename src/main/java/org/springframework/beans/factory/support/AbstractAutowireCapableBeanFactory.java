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

    // ����ʵ�������ԣ�Ĭ��ʹ��SimpleInstantiationStrategy
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeanException {
        return doCreateBean(beanName, beanDefinition);
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = null;
        try {
            // ʹ��ʵ�������Դ���beanʵ��
            bean = createBeanInstance(beanDefinition, beanName);
            // Ϊbean�������
            applyPropertyValues(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeanException("Instantiation of bean failed", e);
        }

        // ע�ᵥ��
        registerSingleton(beanName, bean);
        return bean;
    }

    /**
     * Ϊbean�������
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
                    // beanA����beanB����ʵ����beanB
                    // ����BeanReference����
                    BeanReference beanReference = (BeanReference) value;
                    // ��ȡ���õ�beanʵ��
                    value = getBean(beanReference.getBeanName());

                }

                // ʹ��ͨ�õ������ֶ�ֵ����
                setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            throw new BeanException("Error setting property values for bean: " + beanName, e);
        }
    }

    /**
     * �����ֶ�ֵ��֧�ֶ�������
     * 
     * @param bean      Bean����
     * @param fieldName �ֶ���
     * @param value     �ֶ�ֵ
     */

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void setFieldValue(Object bean, String fieldName, Object value) {
        if (bean == null || fieldName == null) {
            return;
        }

        // ���ݶ������ͷֱ���
        if (bean instanceof Map) {
            // ����Map����
            ((Map) bean).put(fieldName, value);
        } else if (bean instanceof List) {
            // ����List���ͣ����Խ�fieldNameת��Ϊ��������
            try {
                int index = Integer.parseInt(fieldName);
                List list = (List) bean;
                // ȷ��List��С�㹻
                while (list.size() <= index) {
                    list.add(null);
                }
                list.set(index, value);
            } catch (NumberFormatException e) {
                throw new BeanException("Error setting property to List: " + fieldName + " is not a valid index", e);
            }
        } else {
            // ������ͨJava Bean
            try {
                // 1. ���ȳ���setter����
                String setterMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                // ���Ի�ȡsetter����
                Method setterMethod = findSetterMethod(bean.getClass(), setterMethodName, value);
                if (setterMethod != null) {
                    // ʹ��setter��������ֵ
                    setterMethod.setAccessible(true);
                    setterMethod.invoke(bean, value);
                    return;
                }

                // 2. ���û���ҵ�setter����������ֱ�������ֶ�
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
     * ���Һ��ʵ�setter����
     */
    private Method findSetterMethod(Class<?> beanClass, String setterMethodName, Object value) {
        // ��ֱ�Ӳ��Ҿ�ȷƥ���setter����
        if (value != null) {
            try {
                return beanClass.getMethod(setterMethodName, value.getClass());
            } catch (NoSuchMethodException e) {
                // ���������������ܵķ���
            }
        }

        // �������п��ܵ�setter����
        Method[] methods = beanClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(setterMethodName) && method.getParameterCount() == 1) {
                Class<?> paramType = method.getParameterTypes()[0];
                // ���������ͼ�����
                if (value == null || paramType.isInstance(value)) {
                    return method;
                }
            }
        }

        return null;
    }

    /**
     * �����ֶΣ�֧�����ϲ��Ҽ̳в��
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
     * ʹ��ʵ�������Դ���beanʵ��
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