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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModelCollectionPropertyConnector extends ModelPropertyConnector
    implements ICollectionConnector {

  private Map<String, IValueConnector> childConnectors;
  private IModelConnectorFactory       modelConnectorFactory;

  private SelectionChangeSupport       selectionChangeSupport;

  /**
   * Constructs a new model property connector on a model collection property.
   * This constructor does not specify the element class of this collection
   * connector. It must be setted afterwards using the apropriate setter.
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
    childConnectors = new LinkedHashMap<String, IValueConnector>();
    selectionChangeSupport = new SelectionChangeSupport(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addChildConnector(String storageKey, IValueConnector connector) {
    childConnectors.put(storageKey, connector);
    connector.setParentConnector(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addSelectionChangeListener(ISelectionChangeListener listener) {
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
    clonedConnector.childConnectors = new LinkedHashMap<String, IValueConnector>();
    clonedConnector.selectionChangeSupport = new SelectionChangeSupport(
        clonedConnector);
    return clonedConnector;
  }

  /**
   * This implementation returns a <code>ModelConnector</code> instance.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IValueConnector createChildConnector(String connectorId) {
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
    return getChildConnectorKeys().size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getChildConnectorKeys() {
    return childConnectors.keySet();
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
    return selectionChangeSupport.getSelectedIndices();
  }

  /**
   * Before invoking the super implementation which handles the
   * <code>ModelChangeEvent</code>, this implementation reconstructs the child
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
   * <code>ValueChangeEvent</code>, this implementation reconstructs the child
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
    if (evt.getSource() instanceof ISelectionChangeListener) {
      selectionChangeSupport
          .addInhibitedListener((ISelectionChangeListener) evt.getSource());
    }
    try {
      setSelectedIndices(evt.getNewSelection(), evt.getLeadingIndex());
    } finally {
      if (evt.getSource() instanceof ISelectionChangeListener) {
        selectionChangeSupport
            .removeInhibitedListener((ISelectionChangeListener) evt.getSource());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSelectedIndices(int[] newSelectedIndices) {
    selectionChangeSupport.setSelectedIndices(newSelectedIndices);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSelectedIndices(int[] newSelectedIndices, int leadingIndex) {
    selectionChangeSupport.setSelectedIndices(newSelectedIndices, leadingIndex);
  }

  /**
   * Unsupported operation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setTracksChildrenSelection(@SuppressWarnings("unused")
  boolean tracksChildrenSelection) {
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
    IValueConnector removedConnector = childConnectors.remove(storageKey);
    if (removedConnector != null) {
      removedConnector.setParentConnector(null);
      removedConnector.cleanBindings();
      removedConnector.setConnectorValue(null);
    }
  }

  /**
   * Updates the child connectors based on a new model collection.
   */
  private void updateChildConnectors() {
    Collection<?> modelCollection = (Collection<?>) getConnecteeValue();
    int modelCollectionSize = 0;
    if (modelCollection != null && modelCollection.size() > 0) {
      modelCollectionSize = modelCollection.size();
      int i = 0;

      for (Object nextCollectionElement : modelCollection) {
        IValueConnector childConnector = getChildConnector(i);
        if (childConnector == null) {
          childConnector = createChildConnector(getId() + "Element");
          addChildConnector(computeStorageKey(i), childConnector);
        }
        childConnector.setConnectorValue(nextCollectionElement);
        i++;
      }
    }
    while (getChildConnectorCount() != modelCollectionSize) {
      removeChildConnector(computeStorageKey(getChildConnectorCount() - 1));
    }
  }
}
