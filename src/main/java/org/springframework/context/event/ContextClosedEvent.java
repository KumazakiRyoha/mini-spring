package org.springframework.context.event;

import org.springframework.context.ApplicationContext;

/**
 * 应用上下文关闭事件
 * 当ApplicationContext被关闭时发布此事件
 * 
 * @param source
 */
public class ContextClosedEvent extends ApplicationContextEvent {

    /**
     * 构造函数
     * 
     * @param source 事件源，必须是ApplicationContext类型
     */
    public ContextClosedEvent(ApplicationContext source) {
        super(source);
    }

}
