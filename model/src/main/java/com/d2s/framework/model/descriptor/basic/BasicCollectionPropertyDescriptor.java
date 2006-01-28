/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.ICollectionDescriptor;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;

/**
 * Default implementation of a collection descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicCollectionPropertyDescriptor extends
    BasicRelationshipEndPropertyDescriptor implements
    ICollectionPropertyDescriptor {

  private ICollectionDescriptor referencedDescriptor;

  /**
   * Gets the referencedDescriptor.
   * 
   * @return the referencedDescriptor.
   */
  public ICollectionDescriptor getReferencedDescriptor() {
    return referencedDescriptor;
  }

  /**
   * Sets the referencedDescriptor.
   * 
   * @param referencedDescriptor
   *          the referencedDescriptor to set.
   */
  public void setReferencedDescriptor(ICollectionDescriptor referencedDescriptor) {
    this.referencedDescriptor = referencedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public Class getPropertyClass() {
    return getReferencedDescriptor().getCollectionInterface();
  }

  /**
   * {@inheritDoc}
   */
  public ICollectionDescriptor getCollectionDescriptor() {
    return getReferencedDescriptor();
  }
}
