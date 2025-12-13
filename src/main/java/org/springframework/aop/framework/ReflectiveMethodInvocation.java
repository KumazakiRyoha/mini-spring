package org.springframework.aop.framework;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

public class ReflectiveMethodInvocation implements MethodInvocation {

    // 目标对象
    protected final Object target;
    // 目标方法
    protected final Method method;
    // 方法参数
    protected final Object[] arguments;

    public ReflectiveMethodInvocation(Object target, Method method, Object[] arguments) {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return method;
    }

    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public Object proceed() throws Throwable {
        return method.invoke(target, arguments);
    }

    @Override
    public Method getMethod() {
        return method;
    }

}
