package org.springframework.test.common;

import org.springframework.aop.MethodBeforeAdvice;

public class WorldServiceBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(java.lang.reflect.Method method, Object[] args, Object target) throws Throwable {
        System.out.println("BeforeAdvice: do something before the earth explodes");
        System.out.println("  - Method: " + method.getName());
        System.out.println("  - Target: " + target.getClass().getSimpleName());
    }
}
