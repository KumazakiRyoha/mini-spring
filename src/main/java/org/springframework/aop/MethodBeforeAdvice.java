package org.springframework.aop;

import java.lang.reflect.Method;

/**
 * Marker interface for before advice.
 * 
 * @see org.aopalliance.aop.Advice
 * 
 */
public interface MethodBeforeAdvice {

    /**
     * Callback before a given method is invoked.
     * 
     * @param method the method about to be invoked
     * @param args   the arguments to be passed to the method
     * @param target the target of the method invocation
     * @throws Throwable if a problem occurs
     */
    void before(Method method, Object[] args, Object target) throws Throwable;

}
