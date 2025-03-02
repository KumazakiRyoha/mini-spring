package org.springframework.beans.factory.config;

/**
 * Class to be implemented by objects that hold a number of bean definitions.
 */
public class BeanReference {
    private String beanName;

    public BeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return this.beanName;
    }
}
