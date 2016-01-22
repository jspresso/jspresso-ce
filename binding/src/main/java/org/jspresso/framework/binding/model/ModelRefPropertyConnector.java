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
package org.jspresso.framework.binding.model;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import gnu.trove.map.hash.THashMap;

import org.jspresso.framework.binding.ConnectorBindingException;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.IModelChangeListener;
import org.jspresso.framework.model.IModelProvider;
import org.jspresso.framework.model.ModelChangeEvent;
import org.jspresso.framework.model.ModelChangeSupport;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.entity.EntityHelper;
import org.jspresso.framework.model.entity.IEntity;

/**
 * This class is a model property connector which manages a model reference
 * property.
 *
 * @author Vincent Vandenschrick
 */

public class ModelRefPropertyConnector extends ModelPropertyConnector implements
    ICompositeValueConnector, IModelProvider {

  /**
   * {@code THIS_PROPERTY} is a fake property name returning the model
   * itself.
   */
  public static final String           THIS_PROPERTY = "&this";
  private Map<String, IValueConnector> childConnectors;
  private Collection<String>           childConnectorKeys;
  private ModelChangeSupport           modelChangeSupport;

  private final IModelConnectorFactory       modelConnectorFactory;

  /**
   * Constructs a new model property connector on a model reference property.
   *
   * @param modelDescriptor
   *          the model descriptor backing this connector.
   * @param modelConnectorFactory
   *          the factory used to create the property connectors.
   */
  ModelRefPropertyConnector(IComponentDescriptorProvider<?> modelDescriptor,
      IModelConnectorFactory modelConnectorFactory) {
    super(modelDescriptor, modelConnectorFactory.getAccessorFactory());
    this.modelConnectorFactory = modelConnectorFactory;
  }

  private void initChildStructureIfNecessary() {
    if (childConnectors == null) {
      childConnectors = new THashMap<>();
      childConnectorKeys = new ArrayList<>();
    }
  }

  /**
   * The child connectors will use this method to keep track of the referenced
   * model. They will then be notified of the model reference changes.
   * <p>
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
   * {@inheritDoc}
   */
  @Override
  public boolean areChildrenReadable() {
    return isReadable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean areChildrenWritable() {
    // if not set to true, computed reference properties cannot have their
    // nested properties editable unless they are made delegateWritable= true.
    // return true /* isWritable() */;
    if (getModelDescriptor() instanceof IReferencePropertyDescriptor<?>
        && EntityHelper
            .isInlineComponentReference((IReferencePropertyDescriptor<?>) getModelDescriptor())) {
      return isWritable();
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelRefPropertyConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelRefPropertyConnector clone(String newConnectorId) {
    ModelRefPropertyConnector clonedConnector = (ModelRefPropertyConnector) super
        .clone(newConnectorId);
    clonedConnector.modelChangeSupport = null;
    clonedConnector.childConnectors = null;
    clonedConnector.childConnectorKeys = null;
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IValueConnector getChildConnector(String storageKey) {
    String actualKey = storageKey;
    int dashIndex = actualKey.indexOf("#");
    if (dashIndex >= 0) {
      actualKey = actualKey.substring(0, dashIndex);
    }
    if (THIS_PROPERTY.equals(actualKey)) {
      return this;
    }
    int dotIndex = actualKey.indexOf('.');
    if (dotIndex > 0) {
      String root = actualKey.substring(0, dotIndex);
      String nested = actualKey.substring(dotIndex + 1);

      ICompositeValueConnector rootC = (ICompositeValueConnector) getChildConnector(root);
      return rootC.getChildConnector(nested);
    }
    initChildStructureIfNecessary();
    IValueConnector connector = childConnectors.get(actualKey);
    if (connector == null) {
      IComponentDescriptor<?> componentDescriptor = getModelDescriptor()
          .getComponentDescriptor();
      if (componentDescriptor != null) {
        try {
          getSecurityHandler().pushToSecurityContext(componentDescriptor);
          IPropertyDescriptor propertyDescriptor = componentDescriptor
              .getPropertyDescriptor(actualKey);
          if (propertyDescriptor == null) {
            throw new ConnectorBindingException("Property [" + actualKey
                + "] does not exist on {" + componentDescriptor.getName()
                + "}.");
          }
          connector = modelConnectorFactory.createModelConnector(actualKey,
              propertyDescriptor, getSecurityHandler());
        } finally {
          getSecurityHandler().restoreLastSecurityContextSnapshot();
        }
        connector.setParentConnector(this);
        childConnectors.put(actualKey, connector);
        childConnectorKeys.add(actualKey);
      }
    }
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getChildConnectorCount() {
    if (childConnectorKeys == null) {
      return 0;
    }
    return childConnectorKeys.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getChildConnectorKeys() {
    if (childConnectorKeys == null) {
      return Collections.emptyList();
    }
    return new ArrayList<>(childConnectorKeys);
  }

  /**
   * Returns the referenced model.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public <T> T getModel() {
    return getConnectorValue();
  }

  /**
   * Overridden to deal with polymorphism.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IComponentDescriptorProvider<?> getModelDescriptor() {
    IComponentDescriptorProvider<?> registeredModelDescriptor = (IComponentDescriptorProvider<?>) super
        .getModelDescriptor();
    if (getModel() instanceof IEntity
        && !(getModel() instanceof IQueryComponent)) {
      Class<? extends IEntity> entityContract = ((IEntity) getModel())
          .getComponentContract();
      if (!entityContract.equals(registeredModelDescriptor.getModelType())) {
        // we must take care of subclasses (polymorphism)
        return modelConnectorFactory.getDescriptorRegistry()
            .getComponentDescriptor(entityContract);
      }
    }
    return registeredModelDescriptor;
  }

  /**
   * After having performed the standard (super implementation) handling of the
   * {@code ModelChangeEvent}, it will notify its child connectors of the
   * referenced model change.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void modelChange(ModelChangeEvent evt) {
    // notify the listeners
    fireModelChange(getOldConnectorValue(), getConnecteeValue());
    // handle the change normally
    super.modelChange(evt);
  }

  /**
   * The referenced model of this {@code ModelRefPropertyConnector}
   * changed. It will notify its {@code IModelChangeListener} s (i.e. the
   * child property connectors) of the change.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    fireModelChange(evt.getOldValue(), evt.getNewValue());
    super.propertyChange(evt);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readabilityChange() {
    super.readabilityChange();
    for (String key : getChildConnectorKeys()) {
      getChildConnector(key).readabilityChange();
    }
  }

  /**
   * {@inheritDoc}
   *
   * @see #addModelChangeListener(IModelChangeListener)
   */
  @Override
  public void removeModelChangeListener(IModelChangeListener listener) {
    if (modelChangeSupport != null && listener != null) {
      modelChangeSupport.removeModelChangeListener(listener);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writabilityChange() {
    super.writabilityChange();
    for (String key : getChildConnectorKeys()) {
      getChildConnector(key).writabilityChange();
    }
  }

  /**
   * Notifies its listeners that the connector's model changed.
   *
   * @param oldModel
   *          The old model of the connector
   * @param newModel
   *          The new model of the connector
   */
  protected void fireModelChange(Object oldModel, Object newModel) {
    if (modelChangeSupport != null) {
      modelChangeSupport.fireModelChange(oldModel, newModel);
    }
  }

  /**
   * Adds a new child connector to this composite. The key used as storage key
   * is the child connector id.
   *
   * @param childConnector
   *          the added connector.
   */
  @Override
  public final void addChildConnector(IValueConnector childConnector) {
    addChildConnector(childConnector.getId(), childConnector);
  }

  /**
   * Unsupported operation.
   * <p>
   * {@inheritDoc}
   */

  @Override
  public void addChildConnector(String storageKey,
      IValueConnector childConnector) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported operation.
   * <p>
   * {@inheritDoc}
   */

  @Override
  public void removeChildConnector(String storageKey) {
    throw new UnsupportedOperationException();
  }
}
