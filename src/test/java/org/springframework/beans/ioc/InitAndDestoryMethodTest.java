package org.springframework.beans.ioc;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.beans.ioc.bean.Person;

public class InitAndDestoryMethodTest {

    @Test
    public void testInitAndDestroyMethod() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "classpath:init-and-destroy-method.xml");
        
        System.out.println("=== Bean 创建和初始化完成 ===");
        
        // 获取 Person Bean 来验证它已经被正确创建
        Person person = applicationContext.getBean("person", Person.class);
        System.out.println("Person Bean: " + person);
        
        System.out.println("=== 手动关闭容器，触发销毁方法 ===");
        // 手动关闭容器来触发销毁方法
        applicationContext.close();
        
        System.out.println("=== 容器关闭完成 ===");
    }
}
