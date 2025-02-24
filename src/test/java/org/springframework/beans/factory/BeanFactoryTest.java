package org.springframework.beans.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BeanFactoryTest {

    private BeanFactory beanFactory;

    @BeforeEach
    void setUp() {
        beanFactory = new DefaultBeanFactory();
    }

    @Test
    void testRegisterBean() {
        // 创建测试对象
        Person person = new Person("Tom");

        // 注册bean
        beanFactory.registerBean("person", person);

        // 验证bean是否正确注册
        // 注意：需要修改BeanFactory的getBean方法返回Object
        Object result = beanFactory.getBean("person");
        assertNotNull(result);
        assertEquals(person, result);
    }

    @Test
    void testGetBeanNotFound() {
        // 测试获取不存在的bean
        Object result = beanFactory.getBean("notExist");
        assertNull(result);
    }
}