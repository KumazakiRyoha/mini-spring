package org.springframework.beans.ioc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.ioc.service.HelloService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AwareInterfaceTest {

    @Test
    public void testApplicationContextAware() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "classpath:spring.xml");
        HelloService helloService = applicationContext.getBean("helloService", HelloService.class);
        assertThat(helloService.getApplicationContext()).isEqualTo(applicationContext);
        assertThat(helloService.getBeanFactory()).isNotNull();
    }

}
