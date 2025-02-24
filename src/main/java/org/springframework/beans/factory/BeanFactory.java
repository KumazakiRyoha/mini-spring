package org.springframework.beans.factory;

import org.springframework.beans.BeanException;

import java.util.HashMap;
import java.util.Map;

/**
 * Interface to be implemented by objects that hold a number of bean
 * definitions,
 */
public interface BeanFactory {

    Object getBean(String name) throws BeanException;

    void registerBean(String name, Object bean);
}