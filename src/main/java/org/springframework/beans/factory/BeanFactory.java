package org.springframework.beans.factory;

import org.springframework.beans.BeanException;

/**
 * Interface to be implemented by objects that hold a number of bean
 * definitions,
 */
public interface BeanFactory {

    /**
     * Return an instance, which may be shared or independent, of the specified
     * bean.
     * 
     * @param name the name of the bean to retrieve
     * @return an instance of the bean
     * @throws BeanException if the bean could not be created
     */
    Object getBean(String name) throws BeanException;

    /**
     * Return an instance, which may be shared or independent, of the specified
     * 
     * @param <T>          the type of the bean
     * @param name         the name of the bean to retrieve
     * @param requiredType the required type of the bean to retrieve
     * @return
     * @throws BeanException
     */
    <T> T getBean(String name, Class<T> requiredType) throws BeanException;

}