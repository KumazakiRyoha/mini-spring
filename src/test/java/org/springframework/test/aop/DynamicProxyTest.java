package org.springframework.test.aop;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aspectj.weaver.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.AdvisedSupport;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.TargetSource;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.framework.CglibAopProxy;
import org.springframework.aop.framework.JdkDynamicAopProxy;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import org.springframework.test.common.WorldServiceBeforeAdvice;
import org.springframework.test.common.WorldServiceInterceptor;
import org.springframework.test.service.WorldService;
import org.springframework.test.service.WorldServiceImpl;

public class DynamicProxyTest {

    private AdvisedSupport advisedSupport;

    @BeforeEach
    public void setup() {
        WorldService worldService = new WorldServiceImpl();

        advisedSupport = new AdvisedSupport();
        TargetSource targetSource = new TargetSource(worldService);
        WorldServiceInterceptor methodInterceptor = new WorldServiceInterceptor();
        MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* " +
                "org.springframework.test.service.WorldService.explode(..))").getMethodMatcher();
        advisedSupport.setTargetSource(targetSource);
        advisedSupport.setMethodInterceptor(methodInterceptor);
        advisedSupport.setMethodMatcher(methodMatcher);
    }

    @Test
    public void testJdkDynamicProxy() throws Exception {
        WorldService proxy = (WorldService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        proxy.explode();
    }

    @Test
    public void testCglibDynamicProxy() throws Exception {
        WorldService proxy = (WorldService) new CglibAopProxy(advisedSupport).getProxy();
        proxy.explode();
    }

    @Test
    public void testProxyFactory() throws Exception {
        // 测试 JDK 代理
        advisedSupport.setProxyTargetClass(false);
        WorldService jdkProxy = (WorldService) new org.springframework.aop.framework.ProxyFactory(advisedSupport)
                .getProxy();
        jdkProxy.explode();

        // 测试 CGLIB 代理
        advisedSupport.setProxyTargetClass(true);
        WorldService cglibProxy = (WorldService) new org.springframework.aop.framework.ProxyFactory(advisedSupport)
                .getProxy();
        cglibProxy.explode();
    }

    @Test
    public void testBeforeAdvice() throws Exception {
        // 添加前置通知
        WorldServiceBeforeAdvice beforeAdvice = new WorldServiceBeforeAdvice();
        // 用适配器包装前置通知
        MethodBeforeAdviceInterceptor adviceInterceptor = new MethodBeforeAdviceInterceptor(beforeAdvice);
        // 设置到AdvisedSupport中
        advisedSupport.setMethodInterceptor(adviceInterceptor);
        // 创建代理对象
        WorldService proxy = (WorldService) new ProxyFactory(advisedSupport)
                .getProxy();
        proxy.explode();
    }

    @Test
    public void testAdvisor() throws Exception {
        WorldService worldService = new WorldServiceImpl();

        // 1. 创建AdvisedSupport
        String expression = "execution(* org.springframework.test.service.WorldService.explode(..))";
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(expression);

        // 2. 设置通知
        MethodBeforeAdviceInterceptor adviceInterceptor = new MethodBeforeAdviceInterceptor(
                new WorldServiceBeforeAdvice());
        advisor.setAdvice(adviceInterceptor);

        // 3. 检查是否匹配
        ClassFilter classFilter = advisor.getPointcut().getClassFilter();
        if (classFilter.matches(worldService.getClass())) {

            // 4. 创建ProxyFactory
            AdvisedSupport advisedSupport = new AdvisedSupport();
            TargetSource targetSource = new TargetSource(worldService);
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

            // 5. 创建代理对象
            WorldService proxy = (WorldService) new ProxyFactory(advisedSupport)
                    .getProxy();
            proxy.explode();
        }
    }

}
