package org.springframework.beans.ioc.common;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeansException;
<<<<<<< HEAD
import org.springframework.beans.ioc.bean.Car;
=======
import org.springframework.beans.ioc.Car;
>>>>>>> ed89fb3c83e68a6f523c61be323fada2202ec6fa

public class CustomerBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("CustomerBeanPostProcessor#postProcessBeforeInitialization, beanName: " + beanName);
        // 换兰博基尼
        if ("car".equals(beanName)) {
            ((Car) bean).setBrand("lamborghini");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("CustomerBeanPostProcessor#postProcessAfterInitialization, beanName: " + beanName);
        return bean;
    }

}
