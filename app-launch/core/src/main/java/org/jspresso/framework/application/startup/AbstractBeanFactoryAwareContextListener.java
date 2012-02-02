/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.application.startup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;

/**
 * A simple listener to hook in webapp startup that can provide the application
 * bean factory into an arbitrary callback method.
 * 
 * @version $LastChangedRevision: 3701 $
 * @author Vincent Vandenschrick
 */
public abstract class AbstractBeanFactoryAwareContextListener implements
    ServletContextListener {

  /**
   * {@inheritDoc}
   */
  @Override
  public void contextDestroyed(
      @SuppressWarnings("unused") ServletContextEvent event) {
    // No-op
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void contextInitialized(ServletContextEvent event) {
    String beanFactorySelector = getBeanFactorySelector(event);
    String applicationContextKey = getApplicationContextKey(event);
    BeanFactoryLocator bfl = SingletonBeanFactoryLocator
        .getInstance(beanFactorySelector);
    BeanFactoryReference bf = bfl.useBeanFactory(applicationContextKey);
    BeanFactory beanFactory = bf.getFactory();
    contextInitialized(beanFactory, event);
  }

  /**
   * Retrieves the spring bean factory selector to use.
   * 
   * @param event
   *          the servlet context event from which the servlet context can be
   *          retrieved and used.
   * @return the spring bean factory selector to use.
   */
  protected String getBeanFactorySelector(ServletContextEvent event) {
    return event.getServletContext().getInitParameter("beanFactorySelector");
  }

  /**
   * Retrieves the spring application context key to use.
   * 
   * @param event
   *          the servlet context event from which the servlet context can be
   *          retrieved and used.
   * @return the spring application context key to use.
   */
  protected String getApplicationContextKey(ServletContextEvent event) {
    return event.getServletContext().getInitParameter("appContextKey");
  }

  /**
   * Callback method that is executed when the webapp context has been initialized.
   * 
   * @param beanFactory
   *          the bean factory to use.
   * @param event the servlet context initialization event.
   */
  public abstract void contextInitialized(BeanFactory beanFactory, ServletContextEvent event);

}
