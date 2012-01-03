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
package org.jspresso.framework.binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.util.collection.CollectionHelper;
import org.jspresso.framework.util.event.IItemSelectable;
import org.jspresso.framework.util.event.IItemSelectionListener;
import org.jspresso.framework.util.event.ISelectionChangeListener;
import org.jspresso.framework.util.event.ItemSelectionEvent;
import org.jspresso.framework.util.event.SelectionChangeEvent;
import org.jspresso.framework.util.event.SelectionChangeSupport;
import org.jspresso.framework.util.event.ValueChangeEvent;

/**
 * This class is the base class of all default collection connectors. It
 * implements the dynamic management of the child connectors which represent the
 * collection.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractCollectionConnector extends
    AbstractCompositeValueConnector implements ICollectionConnector,
    IItemSelectable {

  private ICompositeValueConnector childConnectorPrototype;
  private IMvcBinder               mvcBinder;

  private List<IValueConnector>    removedChildrenConnectors;
  private SelectionChangeSupport   selectionChangeSupport;

  private List<IValueConnector>    connectorTank;

  /**
   * Creates a new <code>AbstractCollectionConnector</code>.
   * 
   * @param id
   *          the connector id.
   * @param binder
   *          the <code>IMvcBinder</code> used to bind dynamicatlly created
   *          child connectors.
   * @param childConnectorPrototype
   *          the connector prototype used to create new instances of child
   *          connectors.
   */
  public AbstractCollectionConnector(String id, IMvcBinder binder,
      ICompositeValueConnector childConnectorPrototype) {
    super(id);
    this.mvcBinder = binder;
    this.childConnectorPrototype = childConnectorPrototype;
    selectionChangeSupport = new SelectionChangeSupport(this);
    connectorTank = new ArrayList<IValueConnector>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addItemSelectionListener(IItemSelectionListener listener) {
    implAddConnectorSelectionListener(listener);
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
  public AbstractCollectionConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractCollectionConnector clone(String newConnectorId) {
    AbstractCollectionConnector clonedConnector = (AbstractCollectionConnector) super
        .clone(newConnectorId);
    clonedConnector.selectionChangeSupport = new SelectionChangeSupport(
        clonedConnector);
    clonedConnector.removedChildrenConnectors = null;
    clonedConnector.connectorTank = new ArrayList<IValueConnector>();
    return clonedConnector;
  }

  /**
   * creates a new connector cloning the connector prototype.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IValueConnector createChildConnector(String newConnectorId) {
    if (!connectorTank.isEmpty()) {
      return connectorTank.remove(0);
    }
    return childConnectorPrototype.clone(newConnectorId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void fireSelectedItemChange(ItemSelectionEvent evt) {
    implFireSelectedItemChange(evt);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IValueConnector getChildConnector(int index) {
    return getChildConnector(computeStorageKey(index));
  }

  /**
   * Gets the childConnectorPrototype.
   * 
   * @return the childConnectorPrototype.
   */
  public ICompositeValueConnector getChildConnectorPrototype() {
    return childConnectorPrototype;
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
   * {@inheritDoc}
   */
  @Override
  public Object getSelectedItem() {
    return implGetSelectedItem();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeItemSelectionListener(IItemSelectionListener listener) {
    implRemoveConnectorSelectionListener(listener);
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
    int leadingIndex = -1;
    if (newSelectedIndices != null && newSelectedIndices.length > 0) {
      leadingIndex = newSelectedIndices[newSelectedIndices.length - 1];
    }
    setSelectedIndices(newSelectedIndices, leadingIndex);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSelectedIndices(int[] newSelectedIndices, int leadingIndex) {
    int[] oldSelectedIndices = getSelectedIndices();
    selectionChangeSupport.setSelectedIndices(newSelectedIndices, leadingIndex);

    if ((oldSelectedIndices == null && newSelectedIndices != null)
        || (oldSelectedIndices != null && newSelectedIndices == null)
        || (oldSelectedIndices != null && newSelectedIndices != null && !Arrays
            .equals(oldSelectedIndices, newSelectedIndices))) {
      if (newSelectedIndices == null || newSelectedIndices.length == 0) {
        implFireSelectedConnectorChange((IValueConnector) null);
      } else {
        implFireSelectedConnectorChange(getChildConnector(leadingIndex));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTracksChildrenSelection(boolean tracksChildrenSelection) {
    implSetTracksChildrenSelection(tracksChildrenSelection);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return getId();
  }

  /**
   * Dynamically adapts collection of child connectors (child connectors are
   * added or removed depending on the state of the source connector of the
   * event) before calling super implementation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void valueChange(ValueChangeEvent evt) {
    updateChildConnectors();
    super.valueChange(evt);
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

  /**
   * Overrides the default to produce
   * <code>CollectionConnectorValueChangeEvent</code>s.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected ValueChangeEvent createChangeEvent(Object oldConnectorValue,
      Object newConnectorValue) {
    CollectionConnectorValueChangeEvent changeEvent = new CollectionConnectorValueChangeEvent(
        this, oldConnectorValue, newConnectorValue, removedChildrenConnectors);
    removedChildrenConnectors = null;
    return changeEvent;
  }

  private void cleanupConnector(IValueConnector removedConnector) {
    removedConnector.recycle(mvcBinder);
    connectorTank.add(removedConnector);
  }

  /**
   * Updates the child connectors based on a new model collection.
   */
  protected void updateChildConnectors() {
    ICollectionConnector modelConnector = (ICollectionConnector) getModelConnector();
    Map<IValueConnector, List<IValueConnector>> existingConnectorsByModel = new HashMap<IValueConnector, List<IValueConnector>>();

    for (String connectorKey : new ArrayList<String>(getChildConnectorKeys())) {
      IValueConnector childConnector = getChildConnector(connectorKey);
      List<IValueConnector> existingConnectors = existingConnectorsByModel
          .get(childConnector.getModelConnector());
      if (existingConnectors == null) {
        existingConnectors = new ArrayList<IValueConnector>();
        existingConnectorsByModel.put(childConnector.getModelConnector(),
            existingConnectors);
      }
      existingConnectors.add(childConnector);
      removeChildConnector(connectorKey);
    }
    if (modelConnector != null && modelConnector.getChildConnectorCount() > 0) {
      for (int i = 0; i < modelConnector.getChildConnectorCount(); i++) {
        IValueConnector connector;
        IValueConnector nextModelConnector = modelConnector
            .getChildConnector(i);
        List<IValueConnector> existingConnectors = existingConnectorsByModel
            .get(nextModelConnector);
        if (existingConnectors != null && !existingConnectors.isEmpty()) {
          connector = existingConnectors.remove(0);
        } else {
          connector = createChildConnector(getId() + "Element");
          mvcBinder.bind(connector, nextModelConnector);
        }
        addChildConnector(computeStorageKey(i), connector);
      }
    }
    removedChildrenConnectors = new ArrayList<IValueConnector>();
    for (List<IValueConnector> obsoleteConnectors : existingConnectorsByModel
        .values()) {
      for (IValueConnector obsoleteConnector : obsoleteConnectors) {
        cleanupConnector(obsoleteConnector);
        removedChildrenConnectors.add(obsoleteConnector);
      }
    }
  }

  private String computeStorageKey(int i) {
    return CollectionConnectorHelper.computeStorageKey(getId(), i);
  }
}
