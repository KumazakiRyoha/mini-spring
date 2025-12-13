package org.springframework.aop.framework;

import org.springframework.aop.AdvisedSupport;

/**
 * 代理工厂
 */
public class ProxyFactory {

    private AdvisedSupport advisedSupport;

    public ProxyFactory(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    public Object getProxy() {
        // 根据 advisedSupport 决定使用 JDK 代理还是 CGLIB 代理
        if (advisedSupport.isProxyTargetClass()) {
            // 使用 CGLIB 代理
            return new CglibAopProxy(advisedSupport).getProxy();
        } else {
            // 使用 JDK 代理
            return new JdkDynamicAopProxy(advisedSupport).getProxy();
        }
    }

}
