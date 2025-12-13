package org.springframework.aop.framework;

import org.springframework.aop.AdvisedSupport;

import net.sf.cglib.proxy.MethodInterceptor;

/**
 * 动态通知拦截器
 */
public class DynamicAdvisedInterceptor implements MethodInterceptor {

    private final AdvisedSupport advised;

    public DynamicAdvisedInterceptor(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object intercept(Object obj, java.lang.reflect.Method method, Object[] args,
            net.sf.cglib.proxy.MethodProxy proxy) throws Throwable {
                CglibMethodInvocation methodInvocation = new CglibMethodInvocation(
            advised.getTargetSource().getTarget(), method, args, proxy);
        if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
            // 需要拦截
            return advised.getMethodInterceptor().invoke(methodInvocation);
        }
        // 直接调用目标方法
        return methodInvocation.proceed();

}
