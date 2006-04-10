/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.List;

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
  private boolean               manyToMany;
  private List<String>          orderingProperties;

  /**
   * Constructs a new <code>BasicCollectionPropertyDescriptor</code> instance.
   */
  public BasicCollectionPropertyDescriptor() {
    this.manyToMany = false;
  }

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

  /**
   * Gets the manyToMany.
   * 
   * @return the manyToMany.
   */
  public boolean isManyToMany() {
    if (getReverseRelationEnd() != null) {
      // priory ty is given to the reverse relation end.
      return getReverseRelationEnd() instanceof ICollectionPropertyDescriptor;
    }
    return manyToMany;
  }

  /**
   * Sets the manyToMany.
   * 
   * @param manyToMany
   *          the manyToMany to set.
   */
  public void setManyToMany(boolean manyToMany) {
    this.manyToMany = manyToMany;
  }

  /**
   * Gets the orderingProperties.
   * 
   * @return the orderingProperties.
   */
  public List<String> getOrderingProperties() {
    if (orderingProperties == null) {
      return getReferencedDescriptor().getElementDescriptor()
          .getOrderingProperties();
    }
    return orderingProperties;
  }

  /**
   * Sets the orderingProperties.
   * 
   * @param orderingProperties
   *          the orderingProperties to set.
   */
  public void setOrderingProperties(List<String> orderingProperties) {
    this.orderingProperties = orderingProperties;
  }
}
