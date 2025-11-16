package org.springframework.beans.ioc.common.event;

public class EventAndEventListenerTest {
    @org.junit.jupiter.api.Test
    public void testCustomEvent() {
        org.springframework.context.support.ClassPathXmlApplicationContext applicationContext = new org.springframework.context.support.ClassPathXmlApplicationContext(
                "classpath:event-and-event-listener.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext));
        applicationContext.registerShutdownHook();
    }

}
