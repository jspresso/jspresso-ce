/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptorRegistry;

/**
 * Default implementation based on spring application context.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicComponentDescriptorRegistry implements
    IComponentDescriptorRegistry, ApplicationContextAware {

  private ApplicationContext componentApplicationContext;

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor getComponentDescriptor(Class componentContract) {
    return (IComponentDescriptor) componentApplicationContext
        .getBean(componentContract.getName());
  }

  /**
   * Sets the application context holding the component descriptor bean
   * definitions.
   * 
   * @param applicationContext
   *          the application context holding the component descriptor bean
   *          definitions.
   */
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.componentApplicationContext = applicationContext;
  }
}
