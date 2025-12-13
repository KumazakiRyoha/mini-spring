package org.springframework.aop.aspectj;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;

public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    // 切点
    private AspectJExpressionPointcut pointcut;
    // 通知
    private Advice advice;
    // 切点表达式
    private String expression;

    /**
     * 设置切点表达式
     */
    public void setExpression(String expression) {
        this.expression = expression;
        this.pointcut = new AspectJExpressionPointcut(expression);
    }

    /**
     * 获取切点
     */
    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    /**
     * 获取通知
     */
    @Override
    public Advice getAdvice() {
        return advice;
    }

    /**
     * 设置通知
     */
    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

}
