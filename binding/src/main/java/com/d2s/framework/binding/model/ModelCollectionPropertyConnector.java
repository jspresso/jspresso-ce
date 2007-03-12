/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.d2s.framework.binding.ChildConnectorSupport;
import com.d2s.framework.binding.CollectionConnectorHelper;
import com.d2s.framework.binding.ConnectorMap;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.IConnectorMap;
import com.d2s.framework.binding.IConnectorMapProvider;
import com.d2s.framework.binding.IConnectorValueChangeListener;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.ModelChangeEvent;
import com.d2s.framework.model.descriptor.ICollectionDescriptorProvider;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.util.collection.CollectionHelper;
import com.d2s.framework.util.event.ISelectionChangeListener;
import com.d2s.framework.util.event.SelectionChangeEvent;
import com.d2s.framework.util.event.SelectionChangeSupport;

/**
 * This class is a model property connector which manages a collection property.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModelCollectionPropertyConnector extends ModelPropertyConnector
    implements ICollectionConnector, IConnectorMapProvider {

  private IConnectorMap          childConnectors;
  private IModelConnectorFactory modelConnectorFactory;
  private SelectionChangeSupport selectionChangeSupport;
  private ChildConnectorSupport  childConnectorSupport;

  private boolean                needsChildrenUpdate;

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
      ICollectionDescriptorProvider modelDescriptor,
      IModelConnectorFactory modelConnectorFactory) {
    super(modelDescriptor, modelConnectorFactory.getAccessorFactory());
    this.modelConnectorFactory = modelConnectorFactory;
    childConnectors = new ConnectorMap(this);
    childConnectorSupport = new ChildConnectorSupport(this);
    selectionChangeSupport = new SelectionChangeSupport(this);
    needsChildrenUpdate = false;
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
   * Adds a new child connector.
   *
   * @param connector
   *          the connector to be added as composite.
   */
  public void addChildConnector(IValueConnector connector) {
    getConnectorMap().addConnector(connector.getId(), connector);
  }

  /**
   * Removes a child connector.
   *
   * @param connector
   *          the connector to be removed.
   */
  protected void removeChildConnector(IValueConnector connector) {
    getConnectorMap().removeConnector(connector.getId());
    connector.setParentConnector(null);
  }

  /**
   * This implementation returns a <code>ModelConnector</code> instance.
   * <p>
   * {@inheritDoc}
   */
  public IValueConnector createChildConnector(String connectorId) {
    IComponentDescriptor componentDescriptor;
    componentDescriptor = ((ICollectionDescriptorProvider) getModelDescriptor())
        .getCollectionDescriptor().getElementDescriptor();
    IValueConnector elementConnector = modelConnectorFactory
        .createModelConnector(componentDescriptor);
    elementConnector.setId(connectorId);
    return elementConnector;
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
   * Updates its child connectors to reflect the collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void boundAsModel(
      IConnectorValueChangeListener modelConnectorListener,
      PropertyChangeListener readChangeListener,
      PropertyChangeListener writeChangeListener) {
    needsChildrenUpdate = true;
    super.boundAsModel(modelConnectorListener, readChangeListener,
        writeChangeListener);
  }

  private String computeConnectorId(int i) {
    return CollectionConnectorHelper.computeConnectorId(getId(), i);
  }

  /**
   * Updates the child connectors based on a new model collection.
   */
  private void updateChildConnectors() {
    Collection modelCollection = (Collection) getConnecteeValue();
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

  /**
   * {@inheritDoc}
   */
  public IValueConnector getChildConnector(String connectorKey) {
    return childConnectorSupport.getChildConnector(connectorKey);
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getChildConnectorKeys() {
    return childConnectorSupport.getChildConnectorKeys();
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
   * {@inheritDoc}
   */
  @Override
  public ModelCollectionPropertyConnector clone() {
    return clone(getId());
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
  }

  /**
   * {@inheritDoc}
   */
  public void setSelectedIndices(int[] newSelectedIndices, int leadingIndex) {
    selectionChangeSupport.setSelectedIndices(newSelectedIndices, leadingIndex);
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
  public int getChildConnectorCount() {
    return getChildConnectorKeys().size();
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
  public void setAllowLazyChildrenLoading(@SuppressWarnings("unused")
  boolean b) {
    // lazy behaviour can't be turned off.
  }

  /**
   * {@inheritDoc}
   */
  public boolean isAllowLazyChildrenLoading() {
    return true;
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
}
