/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.entity.basic;

import java.util.List;

import com.d2s.framework.model.descriptor.basic.AbstractComponentDescriptor;
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
public class BasicEntityDescriptor extends AbstractComponentDescriptor<IEntity> {

  private boolean purelyAbstract;

  /**
   * Constructs a new <code>BasicEntityDescriptor</code> instance.
   * 
   * @param name
   *            the name of the descriptor which has to be the fully-qualified
   *            class name of its contract.
   */
  public BasicEntityDescriptor(String name) {
    super(name);
    this.purelyAbstract = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getRenderedProperties() {
    List<String> superRenderedProperties = super.getRenderedProperties();
    if (superRenderedProperties != null) {
      superRenderedProperties.remove(IEntity.ID);
      superRenderedProperties.remove(IEntity.VERSION);
    }
    return superRenderedProperties;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isEntity() {
    return true;
  }

  /**
   * Gets the purelyAbstract.
   * 
   * @return the purelyAbstract.
   */
  public boolean isPurelyAbstract() {
    return purelyAbstract;
  }

  /**
   * Sets the purelyAbstract.
   * 
   * @param purelyAbstract
   *            the purelyAbstract to set.
   */
  public void setPurelyAbstract(boolean purelyAbstract) {
    this.purelyAbstract = purelyAbstract;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isComputed() {
    return false;
  }
}
