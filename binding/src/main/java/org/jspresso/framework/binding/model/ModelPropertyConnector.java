/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

import org.jspresso.framework.binding.AbstractValueConnector;
import org.jspresso.framework.binding.ConnectorBindingException;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.model.IModelChangeListener;
import org.jspresso.framework.model.IModelProvider;
import org.jspresso.framework.model.ModelChangeEvent;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptorAware;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.lang.IModelAware;
import org.jspresso.framework.util.lang.ObjectUtils;

/**
 * This connector is a model property connector.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class ModelPropertyConnector extends AbstractValueConnector
    implements IModelChangeListener, PropertyChangeListener {

  private IAccessor        accessor;
  private IAccessorFactory accessorFactory;

  /**
   * Constructs a new model connector on a model property.
   * 
   * @param modelDescriptor
   *          The model descriptor to which the connector is bound at.
   * @param accessorFactory
   *          The factory which is used to build the <code>IAccessor</code> used
   *          to access the java model property bi-directionally
   */
  ModelPropertyConnector(IModelDescriptor modelDescriptor,
      IAccessorFactory accessorFactory) {
    super(modelDescriptor.getName());
    setModelDescriptor(modelDescriptor);
    this.accessorFactory = accessorFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelPropertyConnector clone() {
    return clone(getId());
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
   * Gets the modelProvider.
   * 
   * @return the modelProvider.
   */
  @Override
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
   * Detaches <code>this</code> as <code>PropertyChangeListener</code> on the
   * old model instance and attaches as <code>PropertyChangeListener</code> on
   * the new model instance. When this is done, it notifies its
   * <code>IValueChangeListener</code> s about a possible change on the model
   * property value (the new model property).
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

    if (getReadabilityGates() != null) {
      for (IGate gate : getReadabilityGates()) {
        if (gate instanceof IModelAware) {
          ((IModelAware) gate).setModel(evt.getNewValue());
        }
      }
    }
    if (getWritabilityGates() != null) {
      for (IGate gate : getWritabilityGates()) {
        if (gate instanceof IModelAware) {
          ((IModelAware) gate).setModel(evt.getNewValue());
        }
      }
    }
    writabilityChange();
    readabilityChange();
    fireConnectorValueChange();
  }

  /**
   * Called when the underlying connectee value (the model property) changes.
   * This implementation notifies its <code>IValueChangeListener</code> s about
   * the change passing the new model property.
   * <p>
   * {@inheritDoc}
   */
  public void propertyChange(@SuppressWarnings("unused") PropertyChangeEvent evt) {
    fireConnectorValueChange();
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
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new ConnectorBindingException(ex.getCause());
    } catch (NoSuchMethodException ex) {
      // this may be a normal behaviour in case of polymorphism.
      // don't throw any exception.
      // throw new ConnectorBindingException(ex);
      return null;
    }
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
      try {
        accessor = accessorFactory.createPropertyAccessor(getId(),
            getModelProvider().getModelDescriptor().getModelType());
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      if (accessor instanceof IModelDescriptorAware) {
        ((IModelDescriptorAware) accessor)
            .setModelDescriptor(getModelDescriptor());
      }
    }
    if (oldModelProvider != null) {
      oldModel = oldModelProvider.getModel();
      oldModelProvider.removeModelChangeListener(this);
    }
    if (getModelProvider() != null) {
      getModelProvider().addModelChangeListener(this);
      newModel = getModelProvider().getModel();
    }
    // The following lines are in fact handled by the modelChange call at the
    // end of the method. If we called it twice, it would generate GC problems
    // since the listener would also be added twice.
    // if (oldModel != null && oldModel instanceof IPropertyChangeCapable) {
    // ((IPropertyChangeCapable) oldModel).removePropertyChangeListener(getId(),
    // this);
    // }
    // if (newModel != null && newModel instanceof IPropertyChangeCapable) {
    // ((IPropertyChangeCapable) newModel).addPropertyChangeListener(getId(),
    // this);
    // }
    // line below is mainly used to initialize oldConnectorValue (the model
    // property connector is not used as model yet since it is just being linked
    // to its parent). We would like to use the commented modelChange line but
    // it
    // fails if the current connector is a collection connector.
    // setConnectorValue(getConnecteeValue());
    modelChange(new ModelChangeEvent(getModelProvider(), oldModel, newModel));
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
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new ConnectorBindingException(ex.getCause());
      } catch (NoSuchMethodException ex) {
        throw new ConnectorBindingException(ex);
      }
    }
  }
}
