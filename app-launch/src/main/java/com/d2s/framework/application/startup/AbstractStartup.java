/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.startup;

import java.util.Locale;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;

import com.d2s.framework.application.backend.IBackendController;
import com.d2s.framework.application.frontend.IFrontendController;

/**
 * Abstract class for application startup.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
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
