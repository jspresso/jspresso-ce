/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import com.d2s.framework.model.IModelChangeListener;
import com.d2s.framework.model.IModelProvider;
import com.d2s.framework.model.ModelChangeEvent;
import com.d2s.framework.util.gate.AbstractGate;

/**
 * Base implementation of a model based gate.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractModelGate extends AbstractGate implements
    IModelGate, IModelChangeListener {

  private IModelProvider modelProvider;

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractModelGate clone() {
    AbstractModelGate clonedGate = (AbstractModelGate) super.clone();
    clonedGate.modelProvider = null;
    return clonedGate;
  }

  /**
   * Gets the model held by the model provider.
   * 
   * @return the model held by the model provider.
   */
  protected Object getModel() {
    if (modelProvider != null) {
      return modelProvider.getModel();
    }
    return null;
  }

  /**
   * Gets the modelProvider.
   * 
   * @return the modelProvider.
   */
  public IModelProvider getModelProvider() {
    return modelProvider;
  }

  /**
   * Sets the modelProvider.
   * 
   * @param modelProvider
   *          the modelProvider to set.
   */
  public void setModelProvider(IModelProvider modelProvider) {
    Object oldModel = getModel();
    if (this.modelProvider != null) {
      this.modelProvider.removeModelChangeListener(this);
    }
    this.modelProvider = modelProvider;
    Object newModel = getModel();
    if (this.modelProvider != null) {
      this.modelProvider.addModelChangeListener(this);
    }
    if (getModelProvider() != null) {
      modelChange(new ModelChangeEvent(getModelProvider(), oldModel, newModel));
    }
  }
}
