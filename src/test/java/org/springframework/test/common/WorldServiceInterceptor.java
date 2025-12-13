package org.springframework.test.common;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * WorldService的方法拦截器
 * 用于演示AOP的拦截功能
 * 
 * @author derekyi
 * @date 2020/12/6
 */
public class WorldServiceInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("WorldServiceInterceptor: do something before");
        Object result = invocation.proceed();
        System.out.println("WorldServiceInterceptor: do something after");
        return result;
    }
}
