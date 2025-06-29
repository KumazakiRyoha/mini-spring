package org.springframework.beans.factory;

public interface DisposableBean {

    /**
     * 销毁bean实例
     * 
     * @throws Exception
     */
    void destroy() throws Exception;

}
