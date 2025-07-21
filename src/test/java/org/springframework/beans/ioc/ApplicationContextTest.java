package org.springframework.beans.ioc;

import org.junit.jupiter.api.Test;
<<<<<<< HEAD
import org.springframework.beans.ioc.bean.Car;
import org.springframework.beans.ioc.bean.Person;
=======
>>>>>>> ed89fb3c83e68a6f523c61be323fada2202ec6fa
import org.springframework.context.support.ClassPathXmlApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationContextTest {

    @Test
    public void testApplicationContext() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");

        Person person = applicationContext.getBean("person", Person.class);
        System.out.println(person);
        // name属性在CustomBeanFactoryPostProcessor中被修改为ivy
        assertThat(person.getName()).isEqualTo("ivy");

        Car car = applicationContext.getBean("car", Car.class);
        System.out.println(car);
        // brand属性在CustomerBeanPostProcessor中被修改为lamborghini
        assertThat(car.getBrand()).isEqualTo("lamborghini");
    }
}
