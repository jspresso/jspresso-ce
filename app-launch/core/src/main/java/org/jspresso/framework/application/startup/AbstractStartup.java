/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;


/**
 * Abstract class for application startup.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the actual gui component type used.
 * @param <F>
 *            the actual icon type used.
 * @param <G>
 *            the actual action type used.
 */
public abstract class AbstractStartup<E, F, G> implements IStartup {

  private BeanFactory                  applicationContext;
  private IBackendController           backendController;
  private IFrontendController<E, F, G> frontendController;

  /**
   * Both front and back controllers are retrieved from the spring context,
   * associated and started.
   * <p>
   * {@inheritDoc}
   */
  public void start() {
    // start on brand new instances.
    frontendController = null;
    backendController = null;
    getFrontendController().start(getBackendController(), getStartupLocale());
  }

  /**
   * Gets the applicationContext.
   * 
   * @return the applicationContext.
   */
  protected BeanFactory getApplicationContext() {
    if (applicationContext == null) {
      BeanFactoryLocator bfl = SingletonBeanFactoryLocator.getInstance();
      BeanFactoryReference bf = bfl.useBeanFactory(getApplicationContextKey());
      applicationContext = bf.getFactory();
    }
    return applicationContext;
  }

  /**
   * Gets the application context key to get the application context out of the
   * singleton bean factory locator.
   * 
   * @return the used application context key.
   */
  protected abstract String getApplicationContextKey();

  /**
   * Gets the application backend controller.
   * 
   * @return the application backend controller.
   */
  protected IBackendController getBackendController() {
    if (backendController == null) {
      backendController = (IBackendController) getApplicationContext().getBean(
          "applicationBackController");
    }
    return backendController;

  }

  /**
   * Gets the application frontend controller.
   * 
   * @return the application frontend controller.
   */
  @SuppressWarnings("unchecked")
  protected IFrontendController<E, F, G> getFrontendController() {
    if (frontendController == null) {
      frontendController = (IFrontendController<E, F, G>) getApplicationContext()
          .getBean("applicationFrontController");
    }
    return frontendController;
  }

  /**
   * Gets the startup locale.
   * 
   * @return the startup locale.
   */
  protected abstract Locale getStartupLocale();
}
