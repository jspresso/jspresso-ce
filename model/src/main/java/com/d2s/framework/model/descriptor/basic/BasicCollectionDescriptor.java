/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.ICollectionDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.util.descriptor.DefaultDescriptor;

/**
 * Default implementation of a collection descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicCollectionDescriptor extends DefaultDescriptor implements
    ICollectionDescriptor {

  private Class                collectionInterface;
  private IComponentDescriptor elementDescriptor;

  /**
   * {@inheritDoc}
   */
  public Class getCollectionInterface() {
    return collectionInterface;
  }

  /**
   * Sets the collectionInterface.
   * 
   * @param collectionInterface
   *          the collectionInterface to set.
   */
  public void setCollectionInterface(Class collectionInterface) {
    this.collectionInterface = collectionInterface;
  }

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor getElementDescriptor() {
    return elementDescriptor;
  }

  /**
   * Sets the elementDescriptor.
   * 
   * @param elementDescriptor
   *          the elementDescriptor to set.
   */
  public void setElementDescriptor(IComponentDescriptor elementDescriptor) {
    this.elementDescriptor = elementDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public ICollectionDescriptor getCollectionDescriptor() {
    return this;
  }
}
