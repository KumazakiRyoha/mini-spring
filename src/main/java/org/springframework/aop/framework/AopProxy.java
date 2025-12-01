package org.springframework.aop.framework;

/**
 * AopProxy marker class.
 */
public interface AopProxy {
    /**
     * 获取代理对象
     */
    Object getProxy();
}
