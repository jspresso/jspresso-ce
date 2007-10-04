/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.test;

import junit.framework.TestCase;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;

/**
 * Super-class of all framework test cases.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class D2STestCase extends TestCase {

  private BeanFactory applicationContext;

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
   * gets the key of the bean factory used by the test case as registered in the
   * <code>BeanFactoryLocator</code>.
   * 
   * @return The key of the beanfactory used by the test case.
   */
  protected String getApplicationContextKey() {
    return "com.d2s.framework.test";
  }
}
