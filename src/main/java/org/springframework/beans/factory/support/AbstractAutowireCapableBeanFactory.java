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

            // ִ��bean�ĳ�ʼ��������BeanPostProcessor��ǰ�úͺ��ô�����
            initializeBean(beanName, bean, beanDefinition);

        } catch (Exception e) {
            throw new BeanException("Instantiation of bean failed", e);
        }

        // ע�������ٷ�����bean
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        // ע�ᵥ��
        registerSingleton(beanName, bean);
        return bean;
    }

    /**
     * ע�������ٷ�����bean����bean�̳���DisposableBean�����Զ�������ٷ���
     * 
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        // ���beanʵ����DisposableBean�ӿڣ�ע�����ٷ���
        if (bean instanceof DisposableBean) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }

        // ������Զ�������ٷ�����ע�����ٷ���
        String destroyMethodName = beanDefinition.getDestroyMethodName();
        if (destroyMethodName != null && !destroyMethodName.isEmpty()) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
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

        if (bean instanceof Map) {
            ((Map) bean).put(fieldName, value);
        } else if (bean instanceof List) {
            ((List) bean).add(value);
        } else {
            try {
                // ���ȳ���setter����
                String setterMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                try {
                    Method setter = bean.getClass().getMethod(setterMethodName, value.getClass());
                    setter.invoke(bean, value);
                    return;
                } catch (NoSuchMethodException e) {
                    // ���û���ҵ���ȫƥ���setter�����Բ����������ܵ�setter
                    Method[] methods = bean.getClass().getMethods();
                    for (Method method : methods) {
                        if (method.getName().equals(setterMethodName) && method.getParameterCount() == 1) {
                            // �ҵ�һ��Ǳ�ڵ�setter����������ת��
                            Class<?> paramType = method.getParameterTypes()[0];
                            Object convertedValue = convertValueToType(value, paramType);
                            if (convertedValue != null) {
                                method.invoke(bean, convertedValue);
                                return;
                            }
                        }
                    }
                }

                // ���û���ҵ����ʵ�setter������ֱ�������ֶ�
                java.lang.reflect.Field field = bean.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                // ���Խ�������ת��
                Object convertedValue = convertValueToType(value, field.getType());
                if (convertedValue != null) {
                    field.set(bean, convertedValue);
                } else {
                    // ���ת��ʧ�ܵ�ֵ��Ϊnull������ֱ�����ã����ܻ��׳��쳣��
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
     * ��ֵת��ΪĿ������
     * 
     * @param value      Ҫת����ֵ
     * @param targetType Ŀ������
     * @return ת�����ֵ������޷�ת���򷵻�null
     */
    private Object convertValueToType(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        // ���ֵ�Ѿ���Ŀ�����ͣ�ֱ�ӷ���
        if (targetType.isInstance(value)) {
            return value;
        }

        // String���������͵�ת��
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

        // ��������ת���߼����Ը�����Ҫ���

        return null; // �޷�ת��
    }

    /**
     * ���Һ��ʵ�setter����
     */
    @SuppressWarnings("unused")
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
     * 
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
     * 
     * ִ��bean�ĳ�ʼ��������BeanPostProcessor��ǰ�úͺ��ô�����
     * 
     * @param beanDefinition
     * @param beanName
     * @return
     * @throws Exception
     */
    protected Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        // ִ��BeanPostProcessor��ǰ�ô�����
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        // ִ��bean�ĳ�ʼ������
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition); // �ڶ��ε��ã������ƺ����ظ���
        } catch (Throwable e) {
            throw new BeanException("Invocation of init method failed for bean '" + beanName + "'", e);
        }

        // ִ��BeanPostProcessor�ĺ��ô�����
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);

        return wrappedBean;
    }

    /**
     * ִ��BeanPostProcessor�ĺ��ô�����
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
     * ִ��BeanPostProcessor�ĺ��ô�����
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
     * ִ��bean�ĳ�ʼ������
     * 
     * @param beanDefinition
     * @param beanName
     * @return
     * @throws BeanException if an error occurs during initialization
     */
    protected void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        if (bean instanceof InitializingBean) {
            // ���beanʵ����InitializingBean�ӿڣ�����afterPropertiesSet����
            ((InitializingBean) bean).afterPropertiesSet();
        }
        String initMethodName = beanDefinition.getInitMethodName();
        if (initMethodName != null && !initMethodName.isEmpty()) {
            // ������Զ���ĳ�ʼ��������ʹ�÷������
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            // ��鷽���Ƿ����
            if (initMethod == null) {
                throw new BeanException("No such init method: " + initMethodName + " for bean: " + beanName);
            }
            initMethod.invoke(bean);

        }
    }

    /**
     * 
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