package org.springframework.context;

import org.springframework.beans.factory.Aware;

/**
 * ApplicationContextAware接口允许Bean获取ApplicationContext的引用。
 * 实现此接口的Bean可以通过setApplicationContext方法接收ApplicationContext实例，
 * 从而可以访问Spring容器中的其他Bean和资源。
 * 这是Spring框架中用于实现Bean与ApplicationContext之间解耦的机制。
 * * 注意：实现此接口的Bean需要在Spring容器中注册，才能接收到ApplicationContext实例。
 * 例如，可以在XML配置文件中声明该Bean，或者使用@Component注解进行自动扫描。
 * * 通过实现ApplicationContextAware接口，Bean可以在运行时动态获取ApplicationContext，
 * 从而可以在需要时访问其他Bean或资源。
 * 这对于需要与Spring容器交互的Bean非常有用，例如需要获取
 * 其他Bean的引用或访问容器中的资源。
 * * 实现此接口的Bean通常会在Spring容器启动时被调用，
 * 并在setApplicationContext方法中接收ApplicationContext实例。
 * 这使得Bean可以在初始化时进行一些配置或准备工作。
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext);
}
