package org.springframework.aop;

public class TargetSource {

    private final Object target;

    public TargetSource(Object target) {
        this.target = target;
    }

    /**
     * 获取目标对象实现的接口列表
     * 
     * @return 目标对象实现的接口列表
     */
    public Class<?>[] getTargetClass() {
        return target.getClass().getInterfaces();
    }

    /**
     * 获取目标对象实例
     * 
     * @return 目标对象实例
     */
    public Object getTarget() {
        return target;
    }
}
