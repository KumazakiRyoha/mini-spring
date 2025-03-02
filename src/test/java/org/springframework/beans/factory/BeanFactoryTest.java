package org.springframework.beans.factory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BeanFactoryTest {

    // 用于测试的简单bean类
    public static class TestBean {
        public String greet() {
            return "hello";
        }
    }

    @Test
    public void testSimpleInstantiationStrategy() throws BeanException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new BeanDefinition(TestBean.class);
        factory.registerBeanDefinition("testBean", beanDefinition);

        Object bean = factory.getBean("testBean");
        // 验证使用默认的SimpleInstantiationStrategy创建的bean是TestBean实例
        assertTrue(bean instanceof TestBean);
    }

    @Test
    public void testCglibInstantiationStrategy() throws BeanException {
        // 添加JVM参数检查
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        System.out.println("当前JVM参数: " + arguments);

        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        // 设置CglibSubclassingInstantiationStrategy
        ((AbstractAutowireCapableBeanFactory) factory)
                .setInstantiationStrategy(new CglibSubclassingInstantiationStrategy());

        BeanDefinition beanDefinition = new BeanDefinition(TestBean.class);
        factory.registerBeanDefinition("testBean", beanDefinition);

        Object bean = factory.getBean("testBean");
        // 验证使用Cglib策略创建的bean为TestBean子类（代理类）
        assertTrue(TestBean.class.isAssignableFrom(bean.getClass()));
        assertFalse(bean.getClass().equals(TestBean.class));
    }

    @Test
    public void testPropertyValue() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("foo", "hello"));
        propertyValues.addPropertyValue(new PropertyValue("bar", "world"));
        BeanDefinition beanDefinition = new BeanDefinition(HelloService.class, propertyValues);
        beanFactory.registerBeanDefinition("helloService", beanDefinition);

        HelloService helloService = (HelloService) beanFactory.getBean("helloService");
        System.out.println(helloService.toString());
        assertEquals("hello", helloService.getFoo());
        assertEquals("world", helloService.getBar());
    }

    @Test
    public void testPopulateBeanWithPropertyValues() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name", "derek"));
        propertyValues.addPropertyValue(new PropertyValue("age", 18));
        BeanDefinition beanDefinition = new BeanDefinition(Person.class, propertyValues);
        beanFactory.registerBeanDefinition("person", beanDefinition);

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
        assertEquals("derek", person.getName());
        assertEquals(18, person.getAge());
    }
}
