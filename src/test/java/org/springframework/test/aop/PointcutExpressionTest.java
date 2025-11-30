package org.springframework.test.aop;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.service.HelloService;

public class PointcutExpressionTest {

    @Test
    public void testPointcutExpression() throws NoSuchMethodException, SecurityException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(
                "execution(* org.springframework.test.service.HelloService.sayHello(..))");
        Class<HelloService> clazz = HelloService.class;
        Method method = clazz.getDeclaredMethod("sayHello");
        assertThat(pointcut.matches(clazz)).isTrue();
        assertThat(pointcut.matches(method, clazz)).isTrue();
    }
}
