package org.springframework.beans.factory.support;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * SingletonBeanRegistry接口的默认实现类，提供了单例Bean注册表的基本功能。
 * 使用HashMap存储单例对象，key为beanName，value为对应的单例对象。
 * 实现了注册、获取、判断存在性、获取名称列表、获取数量以及销毁单例等基本操作。
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /**
     * 存储单例对象的容器，key为beanName，value为对应的单例对象
     */
    private final Map<String, Object> singletonObjects = new HashMap<>();

    /**
     * 存储可销毁的单例对象，key为beanName，value为对应的DisposableBean实例
     */
    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    /**
     * 注册单例对象
     * 
     * @param beanName        单例对象的名称
     * @param singletonObject 单例对象
     */
    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        this.singletonObjects.put(beanName, singletonObject);
    }

    /**
     * 注册可销毁的单例对象
     * 
     * @param beanName 单例对象的名称
     * @param bean     可销毁的单例对象
     */
    public void registerDisposableBean(String beanName, DisposableBean bean) {
        this.disposableBeans.put(beanName, bean);
    }

    /**
     * 销毁指定的单例对象
     */
    public void destroySingleton() {
        Set<String> beanNames = disposableBeans.keySet();
        for (String beanName : beanNames) {
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }
        }
    }

    /**
     * 
     * 
     * /**
     * 根据名称获取单例对象
     * 
     * @param beanName 单例对象的名称
     * @return 单例对象，如果不存在则返回null
     */
    @Override
    public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
    }

    /**
     * 判断是否包含指定名称的单例对象
     * 
     * @param beanName 单例对象的名称
     * @return 如果包含返回true，否则返回false
     */
    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

    /**
     * 获取所有单例对象的名称
     * 
     * @return 单例对象名称数组
     */
    @Override
    public String[] getSingletonNames() {
        return this.singletonObjects.keySet().toArray(new String[0]);
    }

    /**
     * 获取单例对象的数量
     * 
     * @return 单例对象的数量
     */
    @Override
    public int getSingletonCount() {
        return this.singletonObjects.size();
    }

    /**
     * 销毁所有单例对象
     */
    @Override
    public void destroySingletons() {
        // 先执行所有Bean的销毁方法
        Set<String> beanNames = new HashSet<>(disposableBeans.keySet());
        for (String beanName : beanNames) {
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }
        }

        // 然后清空单例对象映射
        this.singletonObjects.clear();
    }
}
