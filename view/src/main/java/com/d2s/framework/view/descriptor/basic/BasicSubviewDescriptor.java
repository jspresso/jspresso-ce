/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.Collection;

import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.util.IGate;
import com.d2s.framework.util.descriptor.DefaultIconDescriptor;
import com.d2s.framework.view.descriptor.ISubViewDescriptor;

/**
 * Default implementation of a sub-view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicSubviewDescriptor extends DefaultIconDescriptor implements
    ISubViewDescriptor {

  private IModelDescriptor  modelDescriptor;
  private boolean           readOnly;
  private Collection<IGate> readabilityGates;
  private Collection<IGate> writabilityGates;

  /**
   * Gets the modelDescriptor.
   *
   * @return the modelDescriptor.
   */
  public IModelDescriptor getModelDescriptor() {
    return modelDescriptor;
  }

  /**
   * Sets the modelDescriptor.
   *
   * @param modelDescriptor
   *          the modelDescriptor to set.
   */
  public void setModelDescriptor(IModelDescriptor modelDescriptor) {
    this.modelDescriptor = modelDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isReadOnly() {
    return readOnly;
  }

  /**
   * Sets the readOnly.
   *
   * @param readOnly
   *          the readOnly to set.
   */
  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  /**
   * Gets the readabilityGates.
   *
   * @return the readabilityGates.
   */
  public Collection<IGate> getReadabilityGates() {
    return readabilityGates;
  }

  /**
   * Sets the readabilityGates.
   *
   * @param readabilityGates
   *          the readabilityGates to set.
   */
  public void setReadabilityGates(Collection<IGate> readabilityGates) {
    this.readabilityGates = readabilityGates;
  }

  /**
   * Gets the writabilityGates.
   *
   * @return the writabilityGates.
   */
  public Collection<IGate> getWritabilityGates() {
    return writabilityGates;
  }

  /**
   * Sets the writabilityGates.
   *
   * @param writabilityGates
   *          the writabilityGates to set.
   */
  public void setWritabilityGates(Collection<IGate> writabilityGates) {
    this.writabilityGates = writabilityGates;
  }
}
