package org.springframework.context;

import java.util.List;

import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.io.ResourceLoader;

/**
 * Interface to provide configuration for an application. This is read-only
 * while the application is running, but may be reloaded if the implementation
 * supports this.
 */
public interface ApplicationContext
        extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader {

}
