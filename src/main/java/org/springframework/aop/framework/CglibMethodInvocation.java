package org.springframework.aop.framework;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

/**
 * CGLIB方法调用
 */
public class CglibMethodInvocation extends ReflectiveMethodInvocation {

    private final MethodProxy methodProxy;

    public CglibMethodInvocation(Object target, Method method, Object[] args,
            MethodProxy methodProxy) {
        super(target, method, args);
        this.methodProxy = methodProxy;
    }

    @Override
    public Object proceed() throws Throwable {
        return this.methodProxy.invoke(this.target, this.arguments);
    }

}
