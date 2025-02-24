package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;


/**
 * SingletonBeanRegistry接口的默认实现类，提供了单例Bean注册表的基本功能。
 * 使用HashMap存储单例对象，key为beanName，value为对应的单例对象。
 * 实现了注册、获取、判断存在性、获取名称列表、获取数量以及销毁单例等基本操作。
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private final Map<String, Object> singletonObjects = new HashMap<>();

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        this.singletonObjects.put(beanName, singletonObject);
    }

    @Override
    public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return this.singletonObjects.keySet().toArray(new String[0]);
    }

    @Override
    public int getSingletonCount() {
        return this.singletonObjects.size();
    }

    @Override
    public void destroySingletons() {
        this.singletonObjects.clear();
    }
}
