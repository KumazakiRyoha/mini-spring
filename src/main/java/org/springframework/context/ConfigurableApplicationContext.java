package org.springframework.context;

import org.springframework.beans.factory.support.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext {

    /**
     * 刷新应用上下文，告诉Spring启动应用上下文，并且对上下文中的所有单例bean进行初始化
     * 
     * @throws ApplicationContextException
     */
    void refresh() throws BeansException;

    /**
     * 关闭应用上下文，销毁所有单例bean
     */
    void close() throws BeansException;

    /**
     * 注册一个关闭钩子，在JVM关闭时调用
     */
    void registerShutdownHook();

}
