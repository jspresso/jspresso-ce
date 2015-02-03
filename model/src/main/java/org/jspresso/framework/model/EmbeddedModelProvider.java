/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model;

import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;

/**
 * A simple standalone model provider.
 * 
 * @author Vincent Vandenschrick
 */
public class EmbeddedModelProvider implements IModelProvider {

  private Object                          model;
  private ModelChangeSupport              modelChangeSupport;
  private final IComponentDescriptorProvider<?> modelDescriptor;

  /**
   * Constructs a new {@code EmbeddedModelProvider} instance.
   *
   * @param modelDescriptor
   *          the model descriptor of this model provider.
   */
  public EmbeddedModelProvider(IComponentDescriptorProvider<?> modelDescriptor) {
    this.modelDescriptor = modelDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getModel() {
    return (T) model;
  }

  /**
   * Gets the model descriptor held internally.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IComponentDescriptorProvider<?> getModelDescriptor() {
    return modelDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeModelChangeListener(IModelChangeListener listener) {
    if (listener != null && modelChangeSupport != null) {
      modelChangeSupport.removeModelChangeListener(listener);
    }
  }

  /**
   * Sets a new internally held model instance and forwards the change to all
   * {@code IModelChangeListener}s. In this case this is the enclosing
   * {@code ModelConnector}.
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
