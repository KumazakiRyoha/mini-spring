package org.springframework.beans.ioc.common.event;

public class ContextClosedEventListener implements
        org.springframework.context.ApplicationListener<org.springframework.context.event.ContextClosedEvent> {

    @Override
    public void onApplicationEvent(org.springframework.context.event.ContextClosedEvent event) {
        System.out.println("ContextClosedEvent received: " + event);
    }

}
