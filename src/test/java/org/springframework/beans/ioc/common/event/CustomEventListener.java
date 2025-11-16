package org.springframework.beans.ioc.common.event;

import org.springframework.context.ApplicationListener;

public class CustomEventListener implements ApplicationListener<CustomEvent> {

    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("CustomEvent received: " + event);
    }

}
