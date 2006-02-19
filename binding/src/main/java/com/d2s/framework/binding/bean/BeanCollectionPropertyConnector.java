/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.d2s.framework.binding.ChildConnectorSupport;
import com.d2s.framework.binding.CollectionConnectorHelper;
import com.d2s.framework.binding.ConnectorBindingException;
import com.d2s.framework.binding.ConnectorMap;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.IConnectorMap;
import com.d2s.framework.binding.IConnectorMapProvider;
import com.d2s.framework.binding.IConnectorValueChangeListener;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.util.bean.BeanChangeEvent;
import com.d2s.framework.util.collection.CollectionHelper;
import com.d2s.framework.util.event.ISelectionChangeListener;
import com.d2s.framework.util.event.SelectionChangeEvent;
import com.d2s.framework.util.event.SelectionChangeSupport;

/**
 * This class is a bean property connector which manages a collection property.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanCollectionPropertyConnector extends BeanPropertyConnector
    implements ICollectionConnector, IConnectorMapProvider {

  private IConnectorMap          childConnectors;
  private Class                  elementClass;
  private IBeanConnectorFactory  beanConnectorFactory;
  private SelectionChangeSupport selectionChangeSupport;
  private ChildConnectorSupport  childConnectorSupport;

  /**
   * Constructs a new bean property connector on a bean collection property.
   * This constructor does not specify the element class of this collection
   * connector. It must be setted afterwards using the apropriate setter.
   * 
   * @param property
   *          the property mapped by this connector. This property is also the
   *          connector id.
   * @param beanConnectorFactory
   *          the factory used to create the collection bean connectors.
   */
  public BeanCollectionPropertyConnector(String property,
      IBeanConnectorFactory beanConnectorFactory) {
    super(property, beanConnectorFactory.getAccessorFactory());
    this.beanConnectorFactory = beanConnectorFactory;
    childConnectors = new ConnectorMap(this);
    childConnectorSupport = new ChildConnectorSupport(this);
    selectionChangeSupport = new SelectionChangeSupport(this);
  }

  /**
   * {@inheritDoc}
   */
  public IConnectorMap getConnectorMap() {
    return childConnectors;
  }

  /**
   * Adds a new child connector.
   * 
   * @param connector
   *          the connector to be added as composite.
   */
  public void addChildConnector(IValueConnector connector) {
    childConnectors.addConnector(connector.getId(), connector);
  }

  /**
   * Removes a child connector.
   * 
   * @param connector
   *          the connector to be removed.
   */
  protected void removeChildConnector(IValueConnector connector) {
    childConnectors.removeConnector(connector.getId());
    connector.setParentConnector(null);
  }

  /**
   * This implementation returns a <code>BeanConnector</code> instance.
   * <p>
   * {@inheritDoc}
   */
  public IValueConnector createChildConnector(String connectorId) {
    return beanConnectorFactory.createBeanConnector(connectorId, elementClass);
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
    updateChildConnectors((Collection) evt.getNewValue());
    super.propertyChange(evt);
  }

  /**
   * Before invoking the super implementation which handles the
   * <code>BeanChangeEvent</code>, this implementation reconstructs the child
   * connectors based on the retrieved collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void beanChange(BeanChangeEvent evt) {
    updateChildConnectors((Collection) getConnecteeValue());
    super.beanChange(evt);
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
    if (elementClass == null
        && modelConnectorListener instanceof ICollectionConnector) {
      setElementClass(((ICollectionConnector) modelConnectorListener)
          .getElementClass());
    }
    updateChildConnectors((Collection) getConnecteeValue());
    super.boundAsModel(modelConnectorListener, readChangeListener,
        writeChangeListener);
  }

  private String computeConnectorId(int i) {
    return CollectionConnectorHelper.computeConnectorId(getId(), i);
  }

  /**
   * Updates the child connectors based on a new bean collection.
   * 
   * @param beanCollection
   *          the bean collection to align the child connectors on.
   */
  private void updateChildConnectors(Collection beanCollection) {
    int beanCollectionSize = 0;
    if (beanCollection != null) {
      beanCollectionSize = beanCollection.size();
      int i = 0;
      for (Object nextCollectionElement : beanCollection) {
        if (elementClass == null) {
          throw new ConnectorBindingException(
              "elementClass must be set on BeanCollectionPropertyConnector before it can be used.");
        }
        IValueConnector childConnector = getChildConnector(i);
        if (childConnector == null) {
          childConnector = createChildConnector(computeConnectorId(i));
          addChildConnector(childConnector);
        }
        childConnector.setConnectorValue(nextCollectionElement);
        i++;
      }
    }
    while (getChildConnectorCount() != beanCollectionSize) {
      removeChildConnector(getChildConnector(computeConnectorId(getChildConnectorCount() - 1)));
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
  public BeanCollectionPropertyConnector clone(String newConnectorId) {
    BeanCollectionPropertyConnector clonedConnector = (BeanCollectionPropertyConnector) super
        .clone(newConnectorId);
    clonedConnector.childConnectors = new ConnectorMap(clonedConnector);
    clonedConnector.childConnectorSupport = new ChildConnectorSupport(
        clonedConnector);
    clonedConnector.selectionChangeSupport = new SelectionChangeSupport(
        clonedConnector);
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BeanCollectionPropertyConnector clone() {
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
   * Sets the elementClass.
   * 
   * @param elementClass
   *          the class of the elements (or superclass or interface) contained
   *          in this collection.
   */
  public void setElementClass(Class elementClass) {
    this.elementClass = elementClass;
  }

  /**
   * Gets the elementClass.
   * 
   * @return the elementClass.
   */
  public Class getElementClass() {
    return elementClass;
  }
}
