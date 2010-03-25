/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;

/**
 * Abstract class for application startup.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractStartup implements IStartup {

  /**
   * Logger, available to subclasses.
   */
  private final Log   logger = LogFactory.getLog(getClass());

  private BeanFactory applicationContext;

  /**
   * Gets the applicationContext.
   * 
   * @return the applicationContext.
   */
  protected BeanFactory getApplicationContext() {
    try {
      if (applicationContext == null) {
        BeanFactoryLocator bfl = SingletonBeanFactoryLocator.getInstance();
        BeanFactoryReference bf = bfl
            .useBeanFactory(getApplicationContextKey());
        applicationContext = bf.getFactory();
      }
      return applicationContext;
    } catch (RuntimeException ex) {
      if (getLogger().isErrorEnabled()) {
        getLogger().error(
            getApplicationContextKey() + " context could not be instanciated.",
            ex);
      }
      throw ex;
    }
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
   * Gets the logger.
   * 
   * @return the logger.
   */
  protected Log getLogger() {
    return logger;
  }
}
