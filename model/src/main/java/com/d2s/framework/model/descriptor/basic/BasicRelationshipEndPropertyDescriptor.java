/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import com.d2s.framework.model.descriptor.IRelationshipEndPropertyDescriptor;

/**
 * Default implementation of a relationship descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicRelationshipEndPropertyDescriptor extends
    BasicPropertyDescriptor implements IRelationshipEndPropertyDescriptor {

  private Boolean                            composition;
  private IRelationshipEndPropertyDescriptor reverseRelationEnd;

  /**
   * {@inheritDoc}
   */
  public IRelationshipEndPropertyDescriptor getReverseRelationEnd() {
    if (reverseRelationEnd != null) {
      return reverseRelationEnd;
    }
    if (getParentDescriptor() != null) {
      return ((IRelationshipEndPropertyDescriptor) getParentDescriptor())
          .getReverseRelationEnd();
    }
    return reverseRelationEnd;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isComposition() {
    if (composition != null) {
      return composition.booleanValue();
    }
    if (getParentDescriptor() != null) {
      return ((IRelationshipEndPropertyDescriptor) getParentDescriptor())
          .isComposition();
    }
    return getDefaultComposition();
  }
  
  /**
   * Gets the default composition of a relationship end.
   * 
   * @return the default composition of a relationship end.
   */
  protected abstract boolean getDefaultComposition();

  /**
   * Sets the composition.
   * 
   * @param composition
   *            the composition to set.
   */
  public void setComposition(boolean composition) {
    this.composition = new Boolean(composition);
  }

  /**
   * Sets the reverseRelationEnd property.
   * 
   * @param reverseRelationEnd
   *            the reverseRelationEnd to set.
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

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicRelationshipEndPropertyDescriptor clone() {
    BasicRelationshipEndPropertyDescriptor clonedDescriptor = (BasicRelationshipEndPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }
}
