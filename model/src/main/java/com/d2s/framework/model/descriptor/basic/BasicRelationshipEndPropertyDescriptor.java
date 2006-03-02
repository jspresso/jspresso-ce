/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IRelationshipEndPropertyDescriptor;

/**
 * Default implementation of a relationship descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicRelationshipEndPropertyDescriptor extends
    BasicPropertyDescriptor implements IRelationshipEndPropertyDescriptor {

  private IRelationshipEndPropertyDescriptor reverseRelationEnd;

  /**
   * {@inheritDoc}
   */
  public IRelationshipEndPropertyDescriptor getReverseRelationEnd() {
    return reverseRelationEnd;
  }

  /**
   * Sets the reverseRelationEnd property.
   * 
   * @param reverseRelationEnd
   *          the reverseRelationEnd to set.
   */
  public void setReverseRelationEnd(
      IRelationshipEndPropertyDescriptor reverseRelationEnd) {
    if (this.reverseRelationEnd != reverseRelationEnd) {
      if (this.reverseRelationEnd != null) {
        this.reverseRelationEnd.setReverseRelationEnd(null);
      }
      this.reverseRelationEnd = reverseRelationEnd;
      if (reverseRelationEnd != null) {
        reverseRelationEnd.setReverseRelationEnd(this);
      }
    }
  }
}
