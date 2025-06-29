
package org.springframework.beans.factory;

public interface InitializingBean {

    /**
     * 在bean实例化后，设置所有属性后调用
     * 
     * @throws Exception
     */
    void afterPropertiesSet() throws Exception;
}