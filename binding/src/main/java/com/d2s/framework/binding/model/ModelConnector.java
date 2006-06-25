/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import com.d2s.framework.model.descriptor.IComponentDescriptorProvider;
import com.d2s.framework.util.model.IModelChangeListener;
import com.d2s.framework.util.model.IModelProvider;
import com.d2s.framework.util.model.ModelChangeSupport;

/**
 * This class implements the connector mechanism on an arbitrry model. This type
 * of connector is not targetted at a specific property but at the model
 * instance itself. This implies that the <code>getConnectorValue</code>
 * method returns the model instance itself.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModelConnector extends ModelRefPropertyConnector {

  private InnerModelProvider modelProvider;

  /**
   * Constructs a new instance based on the model class passed as parameter.
   * 
   * @param modelDescriptor
   *          the model descriptor backing this connector.
   * @param modelConnectorFactory
   *          the factory used to create the child property connectors.
   */
  ModelConnector(IComponentDescriptorProvider modelDescriptor,
      IModelConnectorFactory modelConnectorFactory) {
    super(modelDescriptor, modelConnectorFactory);
    this.modelProvider = new InnerModelProvider(modelDescriptor);
    modelProviderChanged(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IModelProvider getModelProvider() {
    return modelProvider;
  }

  /**
   * Returns the model itself (the java model instance).
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getModelProvider().getModel();
  }

  /**
   * Sets the model itself (the java model instance).
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    ((InnerModelProvider) getModelProvider()).setModel(aValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isValueAccessedAsProperty() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelConnector clone(String newConnectorId) {
    ModelConnector clonedConnector = (ModelConnector) super
        .clone(newConnectorId);
    clonedConnector.modelProvider = new InnerModelProvider(
        modelProvider.getModelDescriptor());
    clonedConnector.modelProviderChanged(null);
    return clonedConnector;
  }

  private static final class InnerModelProvider implements IModelProvider {

    private Object                       model;
    private ModelChangeSupport           modelChangeSupport;
    private IComponentDescriptorProvider modelDescriptor;

    private InnerModelProvider(IComponentDescriptorProvider modelDescriptor) {
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
    protected void setModel(Object newModel) {
      Object oldModel = model;
      model = newModel;
      if (modelChangeSupport != null) {
        modelChangeSupport.fireModelChange(oldModel, newModel);
      }
    }
  }
}
