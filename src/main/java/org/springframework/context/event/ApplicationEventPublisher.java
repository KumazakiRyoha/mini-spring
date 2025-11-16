package org.springframework.context.event;

/**
 * 应用事件发布者接口，定义了发布应用事件的基本功能。
 */
public interface ApplicationEventPublisher {

    /**
     * 发布应用事件
     * 
     * @param event
     */
    void publishEvent(ApplicationEvent event);
}