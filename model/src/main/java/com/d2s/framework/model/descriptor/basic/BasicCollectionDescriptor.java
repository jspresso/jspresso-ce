/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.ICollectionDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.util.descriptor.DefaultDescriptor;

/**
 * Default implementation of a collection descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the concrete collection component element type.
 */
public class BasicCollectionDescriptor<E> extends DefaultDescriptor implements
    ICollectionDescriptor<E> {

  private Class<?> collectionInterface;
  private IComponentDescriptor<E>        elementDescriptor;

  /**
   * {@inheritDoc}
   */
  public ICollectionDescriptor<E> getCollectionDescriptor() {
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getCollectionInterface() {
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
  public Class<?> getModelType() {
    return getCollectionInterface();
  }

  /**
   * Sets the collectionInterface.
   * 
   * @param collectionInterface
   *            the collectionInterface to set.
   */
  public void setCollectionInterface(
      Class<?> collectionInterface) {
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
