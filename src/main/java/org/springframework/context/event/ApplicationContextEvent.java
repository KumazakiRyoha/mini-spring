package org.springframework.context.event;

import org.springframework.context.ApplicationContext;

/**
 * 应用上下文事件抽象类
 * 所有应用上下文事件都应继承此类
 */
public abstract class ApplicationContextEvent extends ApplicationEvent {

    /**
     * 构造函数
     * 
     * @param source 事件源，必须是ApplicationContext类型
     */
    public ApplicationContextEvent(ApplicationContext source) {
        super(source);
    }

    /**
     * 获取应用上下文
     * 
     * @return 应用上下文
     */
    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }

}
