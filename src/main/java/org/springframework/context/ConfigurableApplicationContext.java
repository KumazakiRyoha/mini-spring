package org.springframework.context;

public interface ConfigurableApplicationContext extends ApplicationContext {

    /**
     * 刷新应用上下文，告诉Spring启动应用上下文，并且对上下文中的所有单例bean进行初始化
     * 
     * @throws ApplicationContextException
     */
    void refresh() throws ApplicationContextException;

}
