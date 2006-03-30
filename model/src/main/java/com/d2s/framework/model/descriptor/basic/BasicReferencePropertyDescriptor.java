/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;

/**
 * Default implementation of a reference descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicReferencePropertyDescriptor extends
    BasicRelationshipEndPropertyDescriptor implements
    IReferencePropertyDescriptor {

  private IComponentDescriptor referencedDescriptor;

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor getReferencedDescriptor() {
    return referencedDescriptor;
  }

  /**
   * Sets the referencedDescriptor property.
   * 
   * @param referencedDescriptor
   *          the referencedDescriptor to set.
   */
  public void setReferencedDescriptor(IComponentDescriptor referencedDescriptor) {
    this.referencedDescriptor = referencedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public Class getPropertyClass() {
    return getReferencedDescriptor().getComponentContract();
  }

  /**
   * {@inheritDoc}
   */
  public IComponentDescriptor getComponentDescriptor() {
    return getReferencedDescriptor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isQueryable() {
    return true;
  }
}
