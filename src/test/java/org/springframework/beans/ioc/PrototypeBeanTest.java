package org.springframework.beans.ioc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.ioc.bean.Car;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PrototypeBeanTest {

    @Test
    public void testPrototype() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "classpath:prototype-bean.xml");
        Car car1 = applicationContext.getBean("car", Car.class);
        Car car2 = applicationContext.getBean("car", Car.class);
        assertThat(car1 != car2).isTrue();
    }

}
