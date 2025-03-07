package org.springframework.beans.factory.support;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Bean定义读取器的接口。
 * 封装了对配置格式进行高级分析的类
 */
public interface BeanDefinitionReader {

    /**
     * 返回用于注册Bean定义的Bean工厂。
     */
    BeanDefinitionRegistry getRegistry();

    /**
     * 返回用于加载Bean定义的资源加载器。
     */
    ResourceLoader getResourceLoader();

    /**
     * 从指定的资源中读取Bean定义。
     * 
     * @param resources 资源描述符
     * @throws BeanDefinitionStoreException 如果加载或解析出错
     */
    void loadBeanDefinitions(Resource resource) throws BeansException;

    /**
     * 从指定的资源位置读取Bean定义。
     * <p>
     * 如果此Bean定义读取器的ResourceLoader支持，该位置也可以是位置模式。
     * 
     * @param location 资源位置，将使用此Bean定义读取器的ResourceLoader
     *                 （或ResourcePatternResolver）加载
     * @return 找到的Bean定义数量
     * @throws BeanDefinitionStoreException 如果加载或解析出错
     */
    void loadBeanDefinitions(String location) throws BeansException;

    /**
     * 从指定的资源位置读取Bean定义。
     * <p>
     * 
     * @param locations
     * @throws BeansException
     */
    void loadBeanDefinitions(String[] locations) throws BeansException;

}