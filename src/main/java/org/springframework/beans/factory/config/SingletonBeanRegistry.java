package org.springframework.beans.factory.config;

/**
 * 单例bean注册表的接口，定义了单例bean的注册和获取功能。
 */
public interface SingletonBeanRegistry {

    /**
     * 将给定的对象注册为单例，使用给定的bean名称。
     * 
     * @param beanName        单例bean的名称
     * @param singletonObject 要注册的单例对象
     */
    void registerSingleton(String beanName, Object singletonObject);

    /**
     * 根据给定的bean名称获取单例对象。
     * 
     * @param beanName 要查找的bean的名称
     * @return 注册的单例对象，如果不存在则返回null
     */
    Object getSingleton(String beanName);

    /**
     * 判断指定名称的单例bean是否存在于注册表中。
     * 
     * @param beanName 要检查的bean名称
     * @return 如果存在指定名称的单例bean则返回true，否则返回false
     */
    boolean containsSingleton(String beanName);

    /**
     * 返回注册表中所有单例bean的名称。
     * 
     * @return 单例bean名称的数组
     */
    String[] getSingletonNames();

    /**
     * 获取注册表中单例bean的数量。
     * 
     * @return 单例bean的数量
     */
    int getSingletonCount();

    /**
     * 销毁注册表中所有的单例bean。
     */
    void destroySingletons();
}
