/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity.basic;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;
import com.d2s.framework.model.entity.IEntityDescriptorRegistry;

/**
 * TODO Comment needed.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicEntityDescriptorRegistry implements
    IEntityDescriptorRegistry, ApplicationContextAware {

  private ApplicationContext entityApplicationContext;

  /**
   * {@inheritDoc}
   */
  public IEntityDescriptor getEntityDescriptor(Class<?> entityContract) {
    return (IEntityDescriptor) entityApplicationContext.getBean(entityContract
        .getName());
  }

  /**
   * Sets the application context holding the entity descriptor bean
   * definitions.
   * 
   * @param applicationContext
   *          the application context holding the entity descriptor bean
   *          definitions.
   */
  public void setApplicationContext(
      ApplicationContext applicationContext) {
    this.entityApplicationContext = applicationContext;
  }
}
