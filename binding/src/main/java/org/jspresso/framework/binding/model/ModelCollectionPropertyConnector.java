/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
import java.util.List;

import org.jspresso.framework.binding.ChildConnectorSupport;
import org.jspresso.framework.binding.CollectionConnectorHelper;
import org.jspresso.framework.binding.ConnectorMap;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.IConnectorMap;
import org.jspresso.framework.binding.IConnectorMapProvider;
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
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModelCollectionPropertyConnector extends ModelPropertyConnector
    implements ICollectionConnector, IConnectorMapProvider {

  private IConnectorMap          childConnectors;
  private ChildConnectorSupport  childConnectorSupport;
  private IModelConnectorFactory modelConnectorFactory;
  private boolean                needsChildrenUpdate;

  private SelectionChangeSupport selectionChangeSupport;

  /**
   * Constructs a new model property connector on a model collection property.
   * This constructor does not specify the element class of this collection
   * connector. It must be setted afterwards using the apropriate setter.
   * 
   * @param modelDescriptor
   *            the model descriptor backing this connector.
   * @param modelConnectorFactory
   *            the factory used to create the collection model connectors.
   */
  public ModelCollectionPropertyConnector(
      ICollectionDescriptorProvider<?> modelDescriptor,
      IModelConnectorFactory modelConnectorFactory) {
    super(modelDescriptor, modelConnectorFactory.getAccessorFactory());
    this.modelConnectorFactory = modelConnectorFactory;
    childConnectors = new ConnectorMap(this);
    childConnectorSupport = new ChildConnectorSupport(this);
    selectionChangeSupport = new SelectionChangeSupport(this);
    needsChildrenUpdate = false;
  }

  /**
   * Adds a new child connector.
   * 
   * @param connector
   *            the connector to be added as composite.
   */
  public void addChildConnector(IValueConnector connector) {
    getConnectorMap().addConnector(connector.getId(), connector);
  }

  /**
   * {@inheritDoc}
   */
  public void addSelectionChangeListener(ISelectionChangeListener listener) {
    selectionChangeSupport.addSelectionChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  public boolean areChildrenReadable() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean areChildrenWritable() {
    return true;
  }

  /**
   * Updates its child connectors to reflect the collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void boundAsModel() {
    needsChildrenUpdate = true;
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
    clonedConnector.childConnectors = new ConnectorMap(clonedConnector);
    clonedConnector.childConnectorSupport = new ChildConnectorSupport(
        clonedConnector);
    clonedConnector.selectionChangeSupport = new SelectionChangeSupport(
        clonedConnector);
    clonedConnector.needsChildrenUpdate = false;
    return clonedConnector;
  }

  /**
   * This implementation returns a <code>ModelConnector</code> instance.
   * <p>
   * {@inheritDoc}
   */
  public IValueConnector createChildConnector(String connectorId) {
    IComponentDescriptor<?> componentDescriptor;
    componentDescriptor = ((ICollectionDescriptorProvider<?>) getModelDescriptor())
        .getCollectionDescriptor().getElementDescriptor();
    IValueConnector elementConnector = modelConnectorFactory
        .createModelConnector(connectorId, componentDescriptor);
    return elementConnector;
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getChildConnector(int index) {
    return getChildConnector(computeConnectorId(index));
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getChildConnector(String connectorKey) {
    return childConnectorSupport.getChildConnector(connectorKey);
  }

  /**
   * {@inheritDoc}
   */
  public int getChildConnectorCount() {
    return getChildConnectorKeys().size();
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getChildConnectorKeys() {
    return childConnectorSupport.getChildConnectorKeys();
  }

  /**
   * Returns this.
   * <p>
   * {@inheritDoc}
   */
  public ICollectionConnector getCollectionConnector() {
    return this;
  }

  /**
   * Returns singleton list of this.
   * <p>
   * {@inheritDoc}
   */
  public List<ICollectionConnector> getCollectionConnectors() {
    return Collections.singletonList((ICollectionConnector) this);
  }

  /**
   * {@inheritDoc}
   */
  public IConnectorMap getConnectorMap() {
    if (needsChildrenUpdate) {
      updateChildConnectors();
    }
    return childConnectors;
  }

  /**
   * {@inheritDoc}
   */
  public int[] getSelectedIndices() {
    return selectionChangeSupport.getSelectedIndices();
  }

  /**
   * Before invoking the super implementation which handles the
   * <code>ModelChangeEvent</code>, this implementation reconstructs the
   * child connectors based on the retrieved collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void modelChange(ModelChangeEvent evt) {
    needsChildrenUpdate = true;
    super.modelChange(evt);
  }

  /**
   * Before invoking the super implementation which fires the
   * <code>ConnectorValueChangeEvent</code>, this implementation reconstructs
   * the child connectors based on the retrieved collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    needsChildrenUpdate = true;
    super.propertyChange(evt);
  }

  /**
   * {@inheritDoc}
   */
  public void removeSelectionChangeListener(ISelectionChangeListener listener) {
    selectionChangeSupport.removeSelectionChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
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
  public void setSelectedIndices(int[] newSelectedIndices) {
    selectionChangeSupport.setSelectedIndices(newSelectedIndices);
  }

  /**
   * {@inheritDoc}
   */
  public void setSelectedIndices(int[] newSelectedIndices, int leadingIndex) {
    selectionChangeSupport.setSelectedIndices(newSelectedIndices, leadingIndex);
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

  private String computeConnectorId(int i) {
    return CollectionConnectorHelper.computeConnectorId(getId(), i);
  }

  /**
   * Removes a child connector.
   * 
   * @param connector
   *            the connector to be removed.
   */
  private void removeChildConnector(IValueConnector connector) {
    getConnectorMap().removeConnector(connector.getId());
    connector.setParentConnector(null);
    connector.cleanBindings();
    connector.setConnectorValue(null);
  }

  /**
   * Updates the child connectors based on a new model collection.
   */
  private void updateChildConnectors() {
    Collection<?> modelCollection = (Collection<?>) getConnecteeValue();
    needsChildrenUpdate = false;
    int modelCollectionSize = 0;
    if (modelCollection != null && modelCollection.size() > 0) {
      modelCollectionSize = modelCollection.size();
      int i = 0;

      for (Object nextCollectionElement : modelCollection) {
        IValueConnector childConnector = getChildConnector(i);
        if (childConnector == null) {
          childConnector = createChildConnector(computeConnectorId(i));
          addChildConnector(childConnector);
        }
        childConnector.setConnectorValue(nextCollectionElement);
        i++;
      }
    }
    while (getChildConnectorCount() != modelCollectionSize) {
      IValueConnector childConnector = getChildConnector(computeConnectorId(getChildConnectorCount() - 1));
      removeChildConnector(childConnector);
    }
  }
}
