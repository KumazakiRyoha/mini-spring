package org.springframework.context.event;

import org.springframework.context.ApplicationContext;

/**
 * 应用上下文刷新事件
 * 当ApplicationContext被初始化或刷新时发布此事件
 */
public class ContextRefreshedEvent extends ApplicationContextEvent {

    /**
     * 构造函数
     * 
     * @param source 事件源，必须是ApplicationContext类型
     */
    public ContextRefreshedEvent(ApplicationContext source) {
        super(source);
    }

}
