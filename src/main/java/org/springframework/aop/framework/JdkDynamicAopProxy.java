package org.springframework.aop.framework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.aop.AdvisedSupport;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    /**
     * 代理对象的方法被调用时，会执行该方法
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 1. 判断当前方法是否需要拦截
        if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
            // 2. 需要拦截：构造 MethodInvocation，交给拦截器处理
            return advised.getMethodInterceptor().invoke(
                    new ReflectiveMethodInvocation(
                            advised.getTargetSource().getTarget(),
                            method,
                            args));
        } else {
            // 3. 如果不需要拦截，直接调用目标方法
            return method.invoke(advised.getTargetSource().getTarget(), args);
        }
    }

    /**
     * 获取代理对象
     */
    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                this.advised.getTargetSource().getTargetClass(), // 获取目标类的接口
                this); // InvocationHandler就是自己
    }

}
