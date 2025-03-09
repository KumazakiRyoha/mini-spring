package org.springframework.beans.factory.support;

import org.springframework.beans.BeanException;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * BeanDefinitionע���ӿ�
 */
public interface BeanDefinitionRegistry {

    /**
     * ��ע�����עBeanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * �������Ʋ���BeanDefinition
     * 
     * @param beanName
     * @return
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeanException;

    /**
     * �ж��Ƿ����ָ�����Ƶ�BeanDefinition
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * ��ȡָ�����͵�����bean������
     */
    String[] getBeanDefinitionNames();

}
