/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model;

import com.d2s.framework.model.descriptor.IComponentDescriptorProvider;

/**
 * A simple standalone model provider.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EmbeddedModelProvider implements IModelProvider {

  private Object                       model;
  private ModelChangeSupport           modelChangeSupport;
  private IComponentDescriptorProvider modelDescriptor;

  /**
   * Constructs a new <code>EmbeddedModelProvider</code> instance.
   * 
   * @param modelDescriptor
   *          the model descriptor of this model provider.
   */
  public EmbeddedModelProvider(IComponentDescriptorProvider modelDescriptor) {
    this.modelDescriptor = modelDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public void addModelChangeListener(IModelChangeListener listener) {
    if (listener != null) {
      if (modelChangeSupport == null) {
        modelChangeSupport = new ModelChangeSupport(this);
      }
      modelChangeSupport.addModelChangeListener(listener);
    }
  }

  /**
   * Gets the model instance held internally.
   * <p>
   * {@inheritDoc}
   */
  public Object getModel() {
    return model;
  }

  /**
   * Gets the model descriptor held internally.
   * <p>
   * {@inheritDoc}
   */
  public IComponentDescriptorProvider getModelDescriptor() {
    return modelDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public void removeModelChangeListener(IModelChangeListener listener) {
    if (listener != null && modelChangeSupport != null) {
      modelChangeSupport.removeModelChangeListener(listener);
    }
  }

  /**
   * Sets a new internally held model instance and forwards the change to all
   * <code>IModelChangeListener</code>s. In this case this is the enclosing
   * <code>ModelConnector</code>.
   * 
   * @param newModel
   *          the new model instance.
   */
  public void setModel(Object newModel) {
    Object oldModel = model;
    model = newModel;
    if (modelChangeSupport != null) {
      modelChangeSupport.fireModelChange(oldModel, newModel);
    }
  }
}
