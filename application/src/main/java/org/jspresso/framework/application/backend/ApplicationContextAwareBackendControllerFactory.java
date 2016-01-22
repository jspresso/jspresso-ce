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
package org.jspresso.framework.application.backend;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * A simple backend controller factory that leverages a Spring application
 * context to create a new backend controller.
 *
 * @author Vincent Vandenschrick
 */
public class ApplicationContextAwareBackendControllerFactory implements
    IBackendControllerFactory, ApplicationContextAware {

  private ApplicationContext applicationContext;
  private String             backendControllerBeanName;

  /**
   * Retrieves a new backend controller instance from the application context.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IBackendController createBackendController() {
    return applicationContext.getBean(backendControllerBeanName,
        IBackendController.class);
  }

  /**
   * Sets the applicationContext.
   *
   * @param applicationContext
   *          the applicationContext to set.
   */
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Gets the backendControllerBeanName.
   *
   * @return the backendControllerBeanName.
   */
  public String getBackendControllerBeanName() {
    return backendControllerBeanName;
  }

  /**
   * Sets the backendControllerBeanName.
   *
   * @param backendControllerBeanName
   *          the backendControllerBeanName to set.
   */
  public void setBackendControllerBeanName(String backendControllerBeanName) {
    this.backendControllerBeanName = backendControllerBeanName;
  }

}
