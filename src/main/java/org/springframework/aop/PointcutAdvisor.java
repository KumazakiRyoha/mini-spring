package org.springframework.aop;

/**
 * PointcutAdvisor接口
 * 用于将切点(Pointcut)和通知(Advice)结合在一起
 * 
 */
public interface PointcutAdvisor extends Advisor {

    /**
     * 获取切点
     * 
     * @return
     */
    Pointcut getPointcut();

}
