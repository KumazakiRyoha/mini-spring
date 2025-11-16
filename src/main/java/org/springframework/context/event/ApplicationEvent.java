package org.springframework.context.event;

import java.util.EventObject;

/**
 * 应用事件抽象类
 * 所有应用事件都应继承此类
 */
public abstract class ApplicationEvent extends EventObject {

    /**
     * 时间发生的时间戳
     */
    private final long timestamp;

    /**
     * 构造函数
     */
    public ApplicationEvent(Object source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 获取时间戳
     */
    public final long getTimestamp() {
        return this.timestamp;
    }

}
