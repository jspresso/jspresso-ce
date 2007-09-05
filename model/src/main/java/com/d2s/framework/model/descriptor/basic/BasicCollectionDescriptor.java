/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.Collection;

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
 * @param <E>
 *            the concrete collection component element type.
 */
public class BasicCollectionDescriptor<E> extends DefaultDescriptor implements
    ICollectionDescriptor<E> {

  private Class<? extends Collection<? extends E>> collectionInterface;
  private IComponentDescriptor<E>                  elementDescriptor;

  /**
   * {@inheritDoc}
   */
  public ICollectionDescriptor<E> getCollectionDescriptor() {
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Class<? extends Collection<? extends E>> getCollectionInterface() {
    return collectionInterface;
  }

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor<E> getElementDescriptor() {
    return elementDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public Class<? extends Collection<?>> getModelType() {
    return getCollectionInterface();
  }

  /**
   * Sets the collectionInterface.
   * 
   * @param collectionInterface
   *            the collectionInterface to set.
   */
  public void setCollectionInterface(
      Class<? extends Collection<? extends E>> collectionInterface) {
    this.collectionInterface = collectionInterface;
  }

  /**
   * Sets the elementDescriptor.
   * 
   * @param elementDescriptor
   *            the elementDescriptor to set.
   */
  public void setElementDescriptor(IComponentDescriptor<E> elementDescriptor) {
    this.elementDescriptor = elementDescriptor;
  }
}
