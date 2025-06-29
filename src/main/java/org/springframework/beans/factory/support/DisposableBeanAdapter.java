package org.springframework.beans.factory.support;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanDefinition;

public class DisposableBeanAdapter implements DisposableBean {

    private final String beanName;
    private final Object bean;
    private final String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    @Override
    public void destroy() throws Exception {
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }
        // 避免同时继承自DisposableBean，且自定义方法与DisposableBean方法同名，销毁方法执行两次的情况
        if ((destroyMethodName != null && !destroyMethodName.isEmpty())
                && !(bean instanceof DisposableBean && "destroy".equals(destroyMethodName))) {
            try {
                // 执行自定义方法
                bean.getClass().getMethod(destroyMethodName).invoke(bean);
            } catch (Exception e) {
                throw new Exception("Destroy method '" + destroyMethodName + "' on bean with name '" + beanName
                        + "' threw an exception", e);
            }

        }
    }

    public String getBeanName() {
        return beanName;
    }

}
