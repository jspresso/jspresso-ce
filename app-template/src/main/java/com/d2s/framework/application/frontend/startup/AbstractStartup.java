/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.startup;

import java.util.Locale;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;

import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.frontend.IFrontendController;
import com.d2s.framework.application.startup.IStartup;

/**
 * Abstract class for application startup.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractStartup implements IStartup {

  private BeanFactory         applicationContext;
  private IBackendController  backendController;
  private IFrontendController frontendController;

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
  protected IFrontendController getFrontendController() {
    if (frontendController == null) {
      frontendController = (IFrontendController) getApplicationContext()
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

  /**
   * Both front and back controllers are retrieved from the spring context,
   * associated and started.
   * <p>
   * {@inheritDoc}
   */
  public void start() {
    getFrontendController().start(getBackendController(), getStartupLocale());
  }
}
