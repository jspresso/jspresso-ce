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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gnu.trove.map.hash.THashMap;

import org.jspresso.framework.binding.CollectionConnectorHelper;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.ModelChangeEvent;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.util.collection.CollectionHelper;
import org.jspresso.framework.util.event.ISelectionChangeListener;
import org.jspresso.framework.util.event.SelectionChangeEvent;
import org.jspresso.framework.util.event.SelectionChangeSupport;

/**
 * This class is a model property connector which manages a collection property.
 *
 * @author Vincent Vandenschrick
 */
public class ModelCollectionPropertyConnector extends ModelPropertyConnector
    implements ICollectionConnector {

  private Map<String, IValueConnector> childConnectors;
  private Collection<String>           childConnectorKeys;
  private final IModelConnectorFactory       modelConnectorFactory;

  private SelectionChangeSupport       selectionChangeSupport;

  private List<IValueConnector>        connectorTank;

  /**
   * Constructs a new model property connector on a model collection property.
   * This constructor does not specify the element class of this collection
   * connector. It must be set afterwards using the appropriate setter.
   *
   * @param modelDescriptor
   *          the model descriptor backing this connector.
   * @param modelConnectorFactory
   *          the factory used to create the collection model connectors.
   */
  public ModelCollectionPropertyConnector(
      ICollectionDescriptorProvider<?> modelDescriptor,
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
   * {@inheritDoc}
   */
  @Override
  public void addChildConnector(String storageKey, IValueConnector connector) {
    initChildStructureIfNecessary();
    childConnectors.put(storageKey, connector);
    connector.setParentConnector(this);
    childConnectorKeys.add(storageKey);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addSelectionChangeListener(ISelectionChangeListener listener) {
    if (selectionChangeSupport == null) {
      selectionChangeSupport = new SelectionChangeSupport(this);
    }
    selectionChangeSupport.addSelectionChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean areChildrenReadable() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean areChildrenWritable() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWritable() {
    // A model property collection connector should never be made read-only,
    // by its parent connector.
    return isLocallyWritable();
  }

  /**
   * Updates its child connectors to reflect the collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void boundAsModel() {
    updateChildConnectors();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelCollectionPropertyConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelCollectionPropertyConnector clone(String newConnectorId) {
    ModelCollectionPropertyConnector clonedConnector = (ModelCollectionPropertyConnector) super
        .clone(newConnectorId);
    clonedConnector.childConnectors = null;
    clonedConnector.childConnectorKeys = null;
    clonedConnector.connectorTank = null;
    clonedConnector.selectionChangeSupport = null;
    return clonedConnector;
  }

  /**
   * This implementation returns a {@code ModelConnector} instance.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IValueConnector createChildConnector(String connectorId) {
    if (connectorTank != null && !connectorTank.isEmpty()) {
      return connectorTank.remove(0);
    }
    IComponentDescriptor<?> componentDescriptor;
    componentDescriptor = ((ICollectionDescriptorProvider<?>) getModelDescriptor())
        .getCollectionDescriptor().getElementDescriptor();
    IValueConnector elementConnector = modelConnectorFactory
        .createModelConnector(connectorId, componentDescriptor,
            getSecurityHandler());
    return elementConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IValueConnector getChildConnector(int index) {
    return getChildConnector(computeStorageKey(index));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IValueConnector getChildConnector(String connectorKey) {
    if (childConnectors == null) {
      return null;
    }
    int lastDotIndex = connectorKey.lastIndexOf('.');
    if (lastDotIndex > 0) {
      String lastNestedConnectorKey = connectorKey.substring(lastDotIndex + 1);
      return childConnectors.get(lastNestedConnectorKey);
    }
    return childConnectors.get(connectorKey);
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
   * Returns this.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public ICollectionConnector getCollectionConnector() {
    return this;
  }

  /**
   * Returns singleton list of this.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public List<ICollectionConnector> getCollectionConnectors() {
    return Collections.singletonList((ICollectionConnector) this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int[] getSelectedIndices() {
    if (selectionChangeSupport == null) {
      return new int[0];
    }
    return selectionChangeSupport.getSelectedIndices();
  }

  /**
   * Before invoking the super implementation which handles the
   * {@code ModelChangeEvent}, this implementation reconstructs the child
   * connectors based on the retrieved collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void modelChange(ModelChangeEvent evt) {
    updateChildConnectors();
    super.modelChange(evt);
  }

  /**
   * Before invoking the super implementation which fires the
   * {@code ValueChangeEvent}, this implementation reconstructs the child
   * connectors based on the retrieved collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    updateChildConnectors();
    super.propertyChange(evt);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readabilityChange() {
    super.writabilityChange();
    for (String key : getChildConnectorKeys()) {
      getChildConnector(key).readabilityChange();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeSelectionChangeListener(ISelectionChangeListener listener) {
    selectionChangeSupport.removeSelectionChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void selectionChange(SelectionChangeEvent evt) {
    boolean isSourceScl = evt.getSource() instanceof ISelectionChangeListener;
    if (isSourceScl && selectionChangeSupport == null) {
      selectionChangeSupport = new SelectionChangeSupport(this);
    }
    if (isSourceScl) {
      selectionChangeSupport
          .addInhibitedListener((ISelectionChangeListener) evt.getSource());
    }
    try {
      setSelectedIndices(evt.getNewSelection(), evt.getLeadingIndex());
    } finally {
      if (isSourceScl) {
        selectionChangeSupport
            .removeInhibitedListener((ISelectionChangeListener) evt.getSource());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSelectedIndices(int... newSelectedIndices) {
    if(selectionChangeSupport == null) {
      selectionChangeSupport = new SelectionChangeSupport(this);
    }
    selectionChangeSupport.setSelectedIndices(newSelectedIndices);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSelectedIndices(int[] newSelectedIndices, int leadingIndex) {
    if(selectionChangeSupport == null) {
      selectionChangeSupport = new SelectionChangeSupport(this);
    }
    selectionChangeSupport.setSelectedIndices(newSelectedIndices, leadingIndex);
  }

  /**
   * Unsupported operation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setTracksChildrenSelection(boolean tracksChildrenSelection) {
    throw new UnsupportedOperationException();
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
   * Takes a snapshot of the collection (does not keep the reference itself).
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object computeOldConnectorValue(Object connectorValue) {
    return CollectionHelper.cloneCollection((Collection<?>) connectorValue);
  }

  private String computeStorageKey(int i) {
    return CollectionConnectorHelper.computeStorageKey(getId(), i);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeChildConnector(String storageKey) {
    if (childConnectors != null) {
      childConnectors.remove(storageKey);
      childConnectorKeys.remove(storageKey);
    }
  }

  private void cleanupConnector(IValueConnector removedConnector) {
    removedConnector.recycle(null);
    if (connectorTank == null) {
      connectorTank = new ArrayList<>();
    }
    connectorTank.add(removedConnector);
  }

  /**
   * Updates the child connectors based on a new model collection.
   */
  private void updateChildConnectors() {
    Collection<?> modelCollection = (Collection<?>) getConnecteeValue();
    Map<Object, List<IValueConnector>> existingConnectorsByValue = new HashMap<>();
    for (String connectorKey : new ArrayList<>(getChildConnectorKeys())) {
      IValueConnector childConnector = getChildConnector(connectorKey);
      List<IValueConnector> existingConnectors = existingConnectorsByValue
          .get(childConnector.getConnectorValue());
      if (existingConnectors == null) {
        existingConnectors = new ArrayList<>();
        existingConnectorsByValue.put(childConnector.getConnectorValue(),
            existingConnectors);
      }
      existingConnectors.add(childConnector);
      removeChildConnector(connectorKey);
    }
    if (modelCollection != null && modelCollection.size() > 0) {
      int i = 0;
      for (Object nextCollectionElement : modelCollection) {
        IValueConnector connector;
        List<IValueConnector> existingConnectors = existingConnectorsByValue
            .get(nextCollectionElement);
        if (existingConnectors != null && !existingConnectors.isEmpty()) {
          connector = existingConnectors.remove(0);
        } else {
          connector = createChildConnector(getId() + "Element");
        }
        // The connector value must be set in every case, since it might be
        // a different instance that equals the old one.
        // see bug #1017
        connector.setConnectorValue(nextCollectionElement);
        addChildConnector(computeStorageKey(i), connector);
        i++;
      }
    }
    for (List<IValueConnector> obsoleteConnectors : existingConnectorsByValue
        .values()) {
      for (IValueConnector obsoleteConnector : obsoleteConnectors) {
        cleanupConnector(obsoleteConnector);
      }
    }
  }
}
