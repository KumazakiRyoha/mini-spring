package org.springframework.test.aop;

import org.junit.jupiter.api.Test;
import org.springframework.aop.AdvisedSupport;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.TargetSource;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.test.common.WorldServiceInterceptor;
import org.springframework.test.service.WorldService;
import org.springframework.test.service.WorldServiceImpl;

public class DynamicProxyTest {

    @Test
    public void testJdkDynamicProxy() {
        // 创建目标对象
        WorldService wordService = new WorldServiceImpl();
        // 创建AdvisedSupport,并设置目标对象和拦截器
        AdvisedSupport advisedSupport = new AdvisedSupport();
        // 2.1 设置目标对象
        advisedSupport.setTargetSource(new TargetSource(wordService));
        // 2.2 设置拦截器（增强逻辑）
        WorldServiceInterceptor interceptor = new WorldServiceInterceptor();
        advisedSupport.setMethodInterceptor(interceptor);
        // 2.3 设置方法匹配器（这里简单起见，设置为对所有方法都拦截）
        MethodMatcher methodMatcher = new AspectJExpressionPointcut(
                "execution(* org.springframework.test.service.WorldService.explode(..))")
                .getMethodMatcher();
        advisedSupport.setMethodMatcher(methodMatcher);

        // 3. 创建JdkDynamicAopProxy
        WorldService proxy = (WorldService) new org.springframework.aop.framework.JdkDynamicAopProxy(advisedSupport)
                .getProxy();
        // 4. 通过代理对象调用方法
        proxy.explode();
    }
}
