package org.springframework.context.event;

import org.springframework.context.ApplicationListener;

/**
 * 应用事件广播器接口，定义了事件广播的基本功能。
 */
public interface ApplicationEventMulticaster {

    /**
     * 广播应用事件的方法。
     * 
     * @param event 应用事件对象
     */
    void multicastEvent(ApplicationEvent event);

    /**
     * 注册应用监听器的方法。
     * 
     * @param listener 应用监听器对象
     */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * 移除应用监听器的方法。
     * 
     * @param listener 应用监听器对象
     */
    void removeApplicationListener(ApplicationListener<?> listener);
}
