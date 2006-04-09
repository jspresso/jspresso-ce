/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.d2s.framework.util.collection.CollectionHelper;
import com.d2s.framework.util.event.ISelectionChangeListener;
import com.d2s.framework.util.event.SelectionChangeEvent;
import com.d2s.framework.util.event.SelectionChangeSupport;

/**
 * This class is the base class of all default collection connectors. It
 * implements the dynamic management of the child connectors which represent the
 * collection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractCollectionConnector extends
    AbstractCompositeValueConnector implements ICollectionConnector,
    IConnectorSelector {

  private IMvcBinder               mvcBinder;
  private ICompositeValueConnector childConnectorPrototype;
  private SelectionChangeSupport   selectionChangeSupport;
  private List<IValueConnector>    removedChildrenConnectors;

  private boolean                  allowLazyChildrenLoading;
  private boolean                  needsChildrenUpdate;
  private boolean                  needsFireConnectorValueChange;
  private boolean                  isDelayedEvent;

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
    allowLazyChildrenLoading = false;
    needsChildrenUpdate = false;
    needsFireConnectorValueChange = false;
    isDelayedEvent = false;
  }

  /**
   * Dynamically adapts collection of child connectors (child connectors are
   * added or removed depending on the state of the source connector of the
   * event) before calling super implementation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void connectorValueChange(ConnectorValueChangeEvent evt) {
    updateChildConnectors();
    super.connectorValueChange(evt);
  }

  /**
   * Overrides the default to produce
   * <code>CollectionConnectorValueChangeEvent</code>s.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected ConnectorValueChangeEvent createChangeEvent(
      Object oldConnectorValue, Object newConnectorValue) {
    CollectionConnectorValueChangeEvent changeEvent = new CollectionConnectorValueChangeEvent(
        this, oldConnectorValue, newConnectorValue, removedChildrenConnectors);
    changeEvent.setDelayedEvent(isDelayedEvent);
    removedChildrenConnectors = null;
    return changeEvent;
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
   * Updates child connectors depending on the state of the model connector.
   */
  protected void updateChildConnectors() {
    needsChildrenUpdate = true;
    if (!allowLazyChildrenLoading) {
      needsChildrenUpdate = false;
      Collection<String> childConnectorsToRemove = new HashSet<String>();
      removedChildrenConnectors = new ArrayList<IValueConnector>();
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
      for (String nextModelConnectorId : childConnectorsToRemove) {
        IValueConnector connectorToRemove = getChildConnector(nextModelConnectorId);
        mvcBinder.bind(connectorToRemove, null);
        removedChildrenConnectors.add(connectorToRemove);
        removeChildConnector(connectorToRemove);
      }
    }
  }

  private String computeConnectorId(int i) {
    return CollectionConnectorHelper.computeConnectorId(getId(), i);
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
  @Override
  public AbstractCollectionConnector clone(String newConnectorId) {
    AbstractCollectionConnector clonedConnector = (AbstractCollectionConnector) super
        .clone(newConnectorId);
    clonedConnector.selectionChangeSupport = new SelectionChangeSupport(
        clonedConnector);
    clonedConnector.removedChildrenConnectors = null;
    clonedConnector.allowLazyChildrenLoading = allowLazyChildrenLoading;
    return clonedConnector;
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
  public IValueConnector getChildConnector(int index) {
    return getChildConnector(computeConnectorId(index));
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
   * Gets the childConnectorPrototype.
   * 
   * @return the childConnectorPrototype.
   */
  public ICompositeValueConnector getChildConnectorPrototype() {
    return childConnectorPrototype;
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
  public void removeSelectionChangeListener(ISelectionChangeListener listener) {
    selectionChangeSupport.removeSelectionChangeListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void setSelectedIndices(int[] newSelectedIndices) {
    selectionChangeSupport.setSelectedIndices(newSelectedIndices);
    if (newSelectedIndices == null || newSelectedIndices.length == 0) {
      implFireSelectedConnectorChange((IValueConnector) null);
    } else {
      implFireSelectedConnectorChange(getChildConnector(newSelectedIndices[0]));
    }
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
  public void selectionChange(SelectionChangeEvent evt) {
    if (evt.getSource() instanceof ISelectionChangeListener) {
      selectionChangeSupport
          .addInhibitedListener((ISelectionChangeListener) evt.getSource());
    }
    try {
      setSelectedIndices(evt.getNewSelection());
    } finally {
      if (evt.getSource() instanceof ISelectionChangeListener) {
        selectionChangeSupport
            .removeInhibitedListener((ISelectionChangeListener) evt.getSource());
      }
    }
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
  @Override
  public String toString() {
    return getId();
  }

  /**
   * {@inheritDoc}
   */
  public void addConnectorSelectionListener(IConnectorSelectionListener listener) {
    implAddConnectorSelectionListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  public void fireSelectedConnectorChange(ConnectorSelectionEvent evt) {
    implFireSelectedConnectorChange(evt);
  }

  /**
   * {@inheritDoc}
   */
  public void removeConnectorSelectionListener(
      IConnectorSelectionListener listener) {
    implRemoveConnectorSelectionListener(listener);
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
  public Collection<String> getChildConnectorKeys() {
    if (allowLazyChildrenLoading) {
      return Collections.<String> emptyList();
    }
    return super.getChildConnectorKeys();
  }

  /**
   * Sets the allowLazyChildrenLoading.
   * 
   * @param allowLazyChildrenLoading
   *          the allowLazyChildrenLoading to set.
   */
  public void setAllowLazyChildrenLoading(boolean allowLazyChildrenLoading) {
    this.allowLazyChildrenLoading = allowLazyChildrenLoading;
    if (needsChildrenUpdate) {
      updateChildConnectors();
    }
    if (needsFireConnectorValueChange) {
      try {
        isDelayedEvent = true;
        fireConnectorValueChange();
      } finally {
        isDelayedEvent = false;
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public boolean isAllowLazyChildrenLoading() {
    return allowLazyChildrenLoading;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void fireConnectorValueChange() {
    needsFireConnectorValueChange = true;
    if (!allowLazyChildrenLoading) {
      needsFireConnectorValueChange = false;
      super.fireConnectorValueChange();
    }
  }

}
