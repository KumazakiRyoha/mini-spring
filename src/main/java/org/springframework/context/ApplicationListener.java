package org.springframework.context;

import java.util.EventListener;

import org.springframework.context.event.ApplicationEvent;

/**
 * 应用程序监听器接口，定义了监听应用程序事件的基本功能。
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    /**
     * 处理应用程序事件的方法。
     * 
     * @param event 应用程序事件对象
     */
    void onApplicationEvent(E event);

}
