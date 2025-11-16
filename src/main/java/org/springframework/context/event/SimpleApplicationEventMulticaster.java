package org.springframework.context.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeansException;
import org.springframework.context.ApplicationListener;

/**
 * 应用事件广播器接口，定义了事件广播的基本功能。
 */
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    @Override
    public void multicastEvent(ApplicationEvent event) {
        for (ApplicationListener<ApplicationEvent> listener : applicationListeners) {
            if (supportsEvent(listener, event)) {
                listener.onApplicationEvent(event);
            }
        }
    }

    protected boolean supportsEvent(ApplicationListener<ApplicationEvent> listener, ApplicationEvent event) {

        // 获取监听器的泛型类型
        Type type = listener.getClass().getGenericInterfaces()[0];
        // 获取监听器实现的接口的泛型参数
        Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];
        String className = actualType.getTypeName();

        Class<?> eventClassName;
        try {
            eventClassName = Class.forName(className);
        } catch (Exception e) {
            throw new BeansException("wrong event class name: " + className);
        }

        // 判断事件类型是否匹配
        return eventClassName.isAssignableFrom(event.getClass());
    }

}