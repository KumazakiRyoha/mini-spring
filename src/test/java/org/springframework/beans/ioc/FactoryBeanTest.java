package org.springframework.beans.ioc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.ioc.bean.Car;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FactoryBeanTest {

    @Test
    public void testFactoryBean() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "classpath:factory-bean.xml");

        Car car = applicationContext.getBean("car", Car.class);
        assertThat(car.getBrand()).isEqualTo("porsche");
    }
}
