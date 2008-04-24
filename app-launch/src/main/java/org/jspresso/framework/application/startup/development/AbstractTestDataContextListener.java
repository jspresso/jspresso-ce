/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.startup.development;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;

/**
 * A simple listener to hook in webapp startup and persist sample data.
 * <p>
 * Copyright 2005-2008 BlueVox. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractTestDataContextListener implements ServletContextListener {

  /**
   * {@inheritDoc}
   */
  public void contextDestroyed(@SuppressWarnings("unused")
  ServletContextEvent event) {
    // No-op
  }

  /**
   * {@inheritDoc}
   */
  public void contextInitialized(ServletContextEvent event) {
    String applicationContextKey = getApplicationContextKey(event);
    BeanFactoryLocator bfl = SingletonBeanFactoryLocator.getInstance();
    BeanFactoryReference bf = bfl.useBeanFactory(applicationContextKey);
    BeanFactory beanFactory = bf.getFactory();
    if (beanFactory != null) {
      persistTestData(beanFactory);
    }
  }

  /**
   * Triggers the test data creation.
   * 
   * @param beanFactory
   *            the bean factory to use.
   */
  public abstract void persistTestData(BeanFactory beanFactory);

  /**
   * Retrieves the spring application context key to use.
   * 
   * @param event
   *            the servlet context event from which the servlet context can be
   *            retrieved and used.
   * @return the spring application context key to use.
   */
  public String getApplicationContextKey(ServletContextEvent event) {
    return event.getServletContext().getInitParameter("appContextKey");
  }

}
