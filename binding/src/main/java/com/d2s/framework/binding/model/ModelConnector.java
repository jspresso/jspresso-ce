/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

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
   * @param id
   *          the id of this model connector.
   * @param modelClass
   *          the model class on which all the contained model connectors will
   *          act.
   * @param modelConnectorFactory
   *          the factory used to create the child property connectors.
   */
  ModelConnector(String id, Class modelClass,
      IModelConnectorFactory modelConnectorFactory) {
    super(id, modelClass, modelConnectorFactory);
    this.modelProvider = new InnerModelProvider(modelClass);
    modelProviderChanged(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IModelProvider getModelProvider() {
    return modelProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class getModelClass() {
    return getModelProvider().getModelClass();
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
    ((InnerModelProvider) getModelProvider())
        .setModel(aValue);
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
    clonedConnector.modelProvider = new InnerModelProvider(modelProvider
        .getModelClass());
    clonedConnector.modelProviderChanged(null);
    return clonedConnector;
  }

  private static final class InnerModelProvider implements IModelProvider {

    private Object             model;
    private ModelChangeSupport modelChangeSupport;
    private Class              modelClass;

    private InnerModelProvider(Class modelClass) {
      this.modelClass = modelClass;
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
     * Gets the model class held internally.
     * <p>
     * {@inheritDoc}
     */
    public Class getModelClass() {
      return modelClass;
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
      if (model != null) {
        modelClass = model.getClass();
      }
      if (modelChangeSupport != null) {
        modelChangeSupport.fireModelChange(oldModel, newModel);
      }
    }
  }
}
