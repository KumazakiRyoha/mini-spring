package org.springframework.test.common;

import org.aopalliance.intercept.MethodInterceptor;

public class WorldServiceInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(org.aopalliance.intercept.MethodInvocation invocation) throws Throwable {
        // 前置增强
        System.out.println("Before method: " + invocation.getMethod().getName());
        // 调用原始方法
        Object result = invocation.proceed();
        // 后置增强
        System.out.println("After method: " + invocation.getMethod().getName());
        return result;
    }

}
