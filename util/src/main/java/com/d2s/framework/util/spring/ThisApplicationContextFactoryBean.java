/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * This is a simple utility bean to get a reference on the bean factory which
 * loaded it.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ThisApplicationContextFactoryBean implements
    ApplicationContextAware, FactoryBean {

  private ApplicationContext applicationContext;

  /**
   * Returns the application context.
   * <p>
   * {@inheritDoc}
   */
  public Object getObject() throws Exception {
    return applicationContext;
  }

  /**
   * Returns the application context class.
   * <p>
   * {@inheritDoc}
   */
  public Class<?> getObjectType() {
    return ApplicationContext.class;
  }

  /**
   * Returns true.
   * <p>
   * {@inheritDoc}
   */
  public boolean isSingleton() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
