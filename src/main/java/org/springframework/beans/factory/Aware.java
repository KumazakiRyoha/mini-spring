package org.springframework.beans.factory;

/**
 * Marker interface indicating that a bean is eligible for being aware of its
 * own context, such as the ApplicationContext it is in.
 * * <p>
 *     This interface is typically implemented by beans that need to
 *     be aware of their environment or the context in which they are running.
 *     It allows beans to access the ApplicationContext and other
 *     context-related features.
 *    * <p>
 *
 */
public interface Aware {
}
