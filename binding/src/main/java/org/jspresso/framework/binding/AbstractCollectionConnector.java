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
import java.util.HashSet;
import java.util.List;

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
  }

  /**
   * After having called the super implementation, removes the child connector
   * from the cache since it is held by the connector itself.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void addChildConnector(IValueConnector connector) {
    super.addChildConnector(connector);
  }

  /**
   * {@inheritDoc}
   */
  public void addItemSelectionListener(IItemSelectionListener listener) {
    implAddConnectorSelectionListener(listener);
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
  @Override
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
    return clonedConnector;
  }

  /**
   * creates a new connector cloning the connector prototype.
   * <p>
   * {@inheritDoc}
   */
  public IValueConnector createChildConnector(String newConnectorId) {
    return childConnectorPrototype.clone(newConnectorId);
  }

  /**
   * {@inheritDoc}
   */
  public void fireSelectedItemChange(ItemSelectionEvent evt) {
    implFireSelectedItemChange(evt);
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
  @Override
  public Collection<String> getChildConnectorKeys() {
    return super.getChildConnectorKeys();
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
  public int[] getSelectedIndices() {
    return selectionChangeSupport.getSelectedIndices();
  }

  /**
   * {@inheritDoc}
   */
  public Object getSelectedItem() {
    return implGetSelectedItem();
  }

  /**
   * {@inheritDoc}
   */
  public void removeItemSelectionListener(IItemSelectionListener listener) {
    implRemoveConnectorSelectionListener(listener);
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
    int leadingIndex = -1;
    if (newSelectedIndices != null && newSelectedIndices.length > 0) {
      leadingIndex = newSelectedIndices[newSelectedIndices.length - 1];
    }
    setSelectedIndices(newSelectedIndices, leadingIndex);
  }

  /**
   * {@inheritDoc}
   */
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

  /**
   * Updates child connectors depending on the state of the model connector.
   */
  protected void updateChildConnectors() {
    Collection<String> childConnectorsToRemove = new HashSet<String>();
    childConnectorsToRemove.addAll(getChildConnectorKeys());
    if (getModelConnector() != null) {
      int i = 0;
      for (String nextModelConnectorId : ((ICollectionConnector) getModelConnector())
          .getChildConnectorKeys()) {
        childConnectorsToRemove.remove(nextModelConnectorId);
        IValueConnector childModelConnector = ((ICollectionConnector) getModelConnector())
            .getChildConnector(nextModelConnectorId);
        IValueConnector childConnector = getChildConnector(nextModelConnectorId);
        if (childConnector == null) {
          childConnector = createChildConnector(computeConnectorId(i));
          addChildConnector(childConnector);
        }
        mvcBinder.bind(childConnector, childModelConnector);
        i++;
      }
    }
    removedChildrenConnectors = new ArrayList<IValueConnector>();
    for (String nextModelConnectorId : childConnectorsToRemove) {
      IValueConnector connectorToRemove = getChildConnector(nextModelConnectorId);
      mvcBinder.bind(connectorToRemove, null);
      removedChildrenConnectors.add(connectorToRemove);
      removeChildConnector(connectorToRemove);
    }
  }

  private String computeConnectorId(int i) {
    return CollectionConnectorHelper.computeConnectorId(getId(), i);
  }
}
