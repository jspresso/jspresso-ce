/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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

import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Abstract class for application startup.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractStartup implements IStartup {

  private BeanFactory         applicationContext;

  private static final Logger LOG  = LoggerFactory
                                       .getLogger(AbstractStartup.class);

  private static final Object LOCK = new Object();

  /**
   * Gets the applicationContext.
   *
   * @return the applicationContext.
   */
  protected BeanFactory getApplicationContext() {
    try {
      if (applicationContext == null) {
        synchronized (LOCK) {
          BeanFactoryLocator bfl = SingletonBeanFactoryLocator
              .getInstance(getBeanFactorySelector());
          BeanFactoryReference bf = bfl
              .useBeanFactory(getApplicationContextKey());
          applicationContext = bf.getFactory();
          if (applicationContext instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext) applicationContext)
                .registerShutdownHook();
          }
        }
      }
      return applicationContext;
    } catch (RuntimeException ex) {
      LOG.error("{} context could not be instantiated.",
          getApplicationContextKey(), ex);
      throw ex;
    }
  }

  /**
   * Allows to change the default bean factory selector.
   *
   * @return null by default, which means that
   *         {@code classpath*:beanRefFactory.xml} is used.
   */
  protected String getBeanFactorySelector() {
    return null;
  }

  /**
   * Gets the application context key to get the application context out of the
   * singleton bean factory locator.
   *
   * @return the used application context key.
   */
  protected abstract String getApplicationContextKey();

  /**
   * Gets the startup locale.
   *
   * @return the startup locale.
   */
  protected abstract Locale getStartupLocale();

  /**
   * Gets the client timezone.
   *
   * @return the client timezone.
   */
  protected abstract TimeZone getClientTimeZone();
}
