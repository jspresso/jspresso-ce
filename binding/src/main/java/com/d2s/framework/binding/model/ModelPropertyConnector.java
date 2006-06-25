/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.ObjectUtils;

import com.d2s.framework.binding.AbstractValueConnector;
import com.d2s.framework.binding.ConnectorBindingException;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.util.accessor.IAccessor;
import com.d2s.framework.util.accessor.IAccessorFactory;
import com.d2s.framework.util.bean.IPropertyChangeCapable;
import com.d2s.framework.util.model.IModelChangeListener;
import com.d2s.framework.util.model.IModelProvider;
import com.d2s.framework.util.model.ModelChangeEvent;

/**
 * This connector is a model property connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class ModelPropertyConnector extends AbstractValueConnector
    implements IModelValueConnector, IModelChangeListener,
    PropertyChangeListener {

  private IAccessor        accessor;
  private IAccessorFactory accessorFactory;
  private IModelDescriptor modelDescriptor;

  /**
   * Constructs a new model connector on a model property.
   * 
   * @param modelDescriptor
   *          The model descriptor to which the connector is bound at.
   * @param accessorFactory
   *          The factory which is used to build the <code>IAccessor</code>
   *          used to access the java model property bi-directionally
   */
  ModelPropertyConnector(IModelDescriptor modelDescriptor,
      IAccessorFactory accessorFactory) {
    super(modelDescriptor.getName());
    this.modelDescriptor = modelDescriptor;
    this.accessorFactory = accessorFactory;
  }

  /**
   * This method must be called whenever the connector's model provider changes.
   * This method performs any necessary cleaning, attachements and notification
   * needed.
   * 
   * @param oldModelProvider
   *          the old model provider or null if none.
   */
  protected void modelProviderChanged(IModelProvider oldModelProvider) {
    Object oldModel = null;
    Object newModel = null;

    if (isValueAccessedAsProperty() && getModelProvider() != null
        && accessor == null && accessorFactory != null) {
      accessor = accessorFactory.createPropertyAccessor(modelDescriptor
          .getName(), getModelProvider().getModelDescriptor().getModelType());
    }
    if (oldModelProvider != null) {
      oldModel = oldModelProvider.getModel();
      oldModelProvider.removeModelChangeListener(this);
    }
    if (getModelProvider() != null) {
      getModelProvider().addModelChangeListener(this);
      newModel = getModelProvider().getModel();
    }
    if (oldModel != null && oldModel instanceof IPropertyChangeCapable) {
      ((IPropertyChangeCapable) oldModel).removePropertyChangeListener(getId(),
          this);
    }
    if (newModel != null && newModel instanceof IPropertyChangeCapable) {
      ((IPropertyChangeCapable) newModel).addPropertyChangeListener(getId(),
          this);
    }

    // line below is mainly used to initialize oldConnectorValue (the model
    // property connector is not used as model yet since it is just being linked
    // to its parent). We would like to use the commented modelChange line but
    // it
    // fails if the current connector is a collection connector.
    // setConnectorValue(getConnecteeValue());
    modelChange(new ModelChangeEvent(getModelProvider(), oldModel, newModel));
  }

  /**
   * Since model provider is usually the parent connector for this kind of
   * connector, this method is overloaded to call the
   * <code>modelProviderChanged</code> method.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setParentConnector(ICompositeValueConnector parentConnector) {
    IModelProvider oldModelProvider = getModelProvider();
    super.setParentConnector(parentConnector);
    modelProviderChanged(oldModelProvider);
  }

  /**
   * Accesses the underlying model property and gets its value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    try {
      return accessor.getValue(getModelProvider().getModel());
    } catch (IllegalAccessException ex) {
      throw new ConnectorBindingException(ex);
    } catch (InvocationTargetException ex) {
      throw new ConnectorBindingException(ex);
    } catch (NoSuchMethodException ex) {
      // this may be a normal behaviour in case of polymorphism.
      // don't throw any exception.
      // throw new ConnectorBindingException(ex);
      return null;
    }
  }

  /**
   * Accesses the underlying model property and sets its value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (!ObjectUtils.equals(aValue, getConnecteeValue())) {
      try {
        accessor.setValue(getModelProvider().getModel(), aValue);
      } catch (IllegalAccessException ex) {
        throw new ConnectorBindingException(ex);
      } catch (InvocationTargetException ex) {
        throw new ConnectorBindingException(ex);
      } catch (NoSuchMethodException ex) {
        throw new ConnectorBindingException(ex);
      }
    }
  }

  /**
   * Detaches <code>this</code> as <code>PropertyChangeListener</code> on
   * the old model instance and attaches as <code>PropertyChangeListener</code>
   * on the new model instance. When this is done, it notifies its
   * <code>IConnectorValueChangeListener</code> s about a possible change on
   * the model property value (the new model property).
   * <p>
   * {@inheritDoc}
   */
  public void modelChange(ModelChangeEvent evt) {

    if (!(getParentConnector() instanceof ICollectionConnector)) {
      if (evt.getOldValue() != null
          && evt.getOldValue() instanceof IPropertyChangeCapable) {
        ((IPropertyChangeCapable) evt.getOldValue())
            .removePropertyChangeListener(getId(), this);
      }
      if (evt.getNewValue() != null
          && evt.getNewValue() instanceof IPropertyChangeCapable) {
        ((IPropertyChangeCapable) evt.getNewValue()).addPropertyChangeListener(
            getId(), this);
      }
    }

    boolean oldWritability;
    boolean newWritability;
    if (evt.getOldValue() != null) {
      oldWritability = super.isWritable();
    } else {
      oldWritability = false;
    }
    if (evt.getNewValue() != null) {
      newWritability = super.isWritable();
    } else {
      newWritability = isWritable();
    }
    firePropertyChange(WRITABLE_PROPERTY, oldWritability, newWritability);
    fireConnectorValueChange();
  }

  /**
   * Called when the underlying connectee value (the model property) changes.
   * This implementation notifies its <code>IConnectorValueChangeListener</code>
   * s about the change passing the new model property.
   * <p>
   * {@inheritDoc}
   */
  public void propertyChange(@SuppressWarnings("unused")
  PropertyChangeEvent evt) {
    fireConnectorValueChange();
  }

  /**
   * Gets the modelProvider.
   * 
   * @return the modelProvider.
   */
  public IModelProvider getModelProvider() {
    if (getParentConnector() instanceof IModelProvider) {
      return (IModelProvider) getParentConnector();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWritable() {
    boolean writable = super.isWritable();
    if (accessor != null) {
      writable = writable && accessor.isWritable();
    }
    if (getModelProvider() != null) {
      writable = writable && getModelProvider().getModel() != null;
    }
    return writable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelPropertyConnector clone(String newConnectorId) {
    ModelPropertyConnector clonedConnector = (ModelPropertyConnector) super
        .clone(newConnectorId);
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelPropertyConnector clone() {
    return clone(getId());
  }

  /**
   * Wether this is a 'real' property connector (a opposed to a ModelConnector).
   * 
   * @return true if this is a 'real' property connector.
   */
  protected boolean isValueAccessedAsProperty() {
    return true;
  }

  /**
   * Gets the modelDescriptor.
   * 
   * @return the modelDescriptor.
   */
  public IModelDescriptor getModelDescriptor() {
    return modelDescriptor;
  }
}
