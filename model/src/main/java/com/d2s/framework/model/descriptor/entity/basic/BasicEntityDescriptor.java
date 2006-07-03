/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.entity.basic;

import java.util.List;

import com.d2s.framework.model.descriptor.basic.BasicComponentDescriptor;
import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;
import com.d2s.framework.model.entity.IEntity;

/**
 * Default implementation of entity descriptors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicEntityDescriptor extends BasicComponentDescriptor implements
    IEntityDescriptor {

  private boolean purelyAbstract;

  /**
   * Constructs a new <code>BasicEntityDescriptor</code> instance.
   * 
   * @param name
   *          the name of the descriptor which has to be the fully-qualified
   *          class name of its contract.
   */
  public BasicEntityDescriptor(String name) {
    super(name);
    this.purelyAbstract = false;
  }

  /**
   * Throws an exception since an entity is always a persistent definition.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setComputed(@SuppressWarnings("unused")
  boolean computed) {
    throw new UnsupportedOperationException(
        "An entity descriptor cannot be a computed interface.");
  }

  /**
   * Gets the purelyAbstract.
   * 
   * @return the purelyAbstract.
   */
  @Override
  public boolean isPurelyAbstract() {
    return purelyAbstract;
  }

  /**
   * Sets the purelyAbstract.
   * 
   * @param purelyAbstract
   *          the purelyAbstract to set.
   */
  public void setPurelyAbstract(boolean purelyAbstract) {
    this.purelyAbstract = purelyAbstract;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Class<? extends IEntity> getComponentContract() {
    return (Class<? extends IEntity>) super.getComponentContract();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getRenderedProperties() {
    List<String> superRenderedProperties = super.getRenderedProperties();
    if (superRenderedProperties != null) {
      superRenderedProperties.remove(IEntity.ID);
    }
    return superRenderedProperties;
  }
}
