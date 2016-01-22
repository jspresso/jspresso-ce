/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
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
