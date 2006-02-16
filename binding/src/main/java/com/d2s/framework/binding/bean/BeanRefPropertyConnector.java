/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.bean;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

import com.d2s.framework.binding.ChildConnectorSupport;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IConnectorMap;
import com.d2s.framework.binding.IConnectorMapProvider;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.util.bean.BeanChangeEvent;
import com.d2s.framework.util.bean.BeanChangeSupport;
import com.d2s.framework.util.bean.IBeanChangeListener;
import com.d2s.framework.util.bean.IBeanProvider;
import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * This class is a bean property connector which manages a bean reference
 * property.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */

public class BeanRefPropertyConnector extends BeanPropertyConnector implements
    ICompositeValueConnector, IConnectorMapProvider, IBeanProvider {

  private IBeanConnectorFactory beanConnectorFactory;
  private Class                 beanClass;
  private BeanChangeSupport     beanChangeSupport;
  private IConnectorMap         childConnectors;
  private ChildConnectorSupport childConnectorSupport;

  /**
   * <code>THIS_PROPERTY</code> is a fake property name returning the bean
   * itself.
   */
  public static final String    THIS_PROPERTY = "&this";

  /**
   * Constructs a new bean property connector on a bean reference property.
   * 
   * @param property
   *          the property mapped by this connector. This property is also the
   *          connector id.
   * @param beanClass
   *          the bean class on which all the contained bean connectors will
   *          act.
   * @param beanConnectorFactory
   *          the factory used to create the property connectors.
   */
  BeanRefPropertyConnector(String property, Class beanClass,
      IBeanConnectorFactory beanConnectorFactory) {
    super(property, beanConnectorFactory.getAccessorFactory());
    this.beanClass = beanClass;
    this.beanConnectorFactory = beanConnectorFactory;
    beanChangeSupport = new BeanChangeSupport(this);
    childConnectors = new BeanConnectorMap(this,
        beanConnectorFactory);
    childConnectorSupport = new ChildConnectorSupport(this);
  }

  /**
   * Returns the child connectors of this <code>BeanRefPropertyConnector</code>
   * instance. This is the lazilly created map of
   * <code>BeanPropertyConnector</code> s connected to the bean properties of
   * the referenced bean.
   * <p>
   * {@inheritDoc}
   */
  public IConnectorMap getConnectorMap() {
    return childConnectors;
  }

  /**
   * The child connectors will use this method to keep track of the referenced
   * bean. They will then be notified of the bean reference changes.
   * <p>
   * {@inheritDoc}
   */
  public void addBeanChangeListener(IBeanChangeListener listener) {
    if (listener != null) {
      beanChangeSupport.addBeanChangeListener(listener);
    }
  }

  /**
   * Returns the class (or superclass) of the referenced bean.
   * <p>
   * {@inheritDoc}
   */
  public Class getBeanClass() {
    return beanClass;
  }

  /**
   * Returns the referenced bean.
   * <p>
   * {@inheritDoc}
   */
  public IPropertyChangeCapable getBean() {
    return (IPropertyChangeCapable) getConnecteeValue();
  }

  /**
   * {@inheritDoc}
   * 
   * @see #addBeanChangeListener(IBeanChangeListener)
   */
  public void removeBeanChangeListener(IBeanChangeListener listener) {
    if (listener != null) {
      beanChangeSupport.removeBeanChangeListener(listener);
    }
  }

  /**
   * The referenced bean of this <code>BeanRefPropertyConnector</code>
   * changed. It will notify its <code>IBeanChangeListener</code> s (i.e. the
   * child property connectors) of the change.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    super.propertyChange(evt);
    fireBeanChange((IPropertyChangeCapable) evt.getOldValue(),
        (IPropertyChangeCapable) evt.getNewValue());
  }

  /**
   * After having performed the standard (super implementation) handling of the
   * <code>BeanChangeEvent</code>, it will notify its child connectors of the
   * referenced bean change.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void beanChange(BeanChangeEvent evt) {
    // preserve the old value before it gets changed.
    Object oldValue = getOldConnectorValue();
    // handle the change normally
    super.beanChange(evt);
    // then notify the listeners
    fireBeanChange((IPropertyChangeCapable) oldValue,
        (IPropertyChangeCapable) getConnecteeValue());
  }

  /**
   * Notifies its listeners that the connector's bean changed.
   * 
   * @param oldBean
   *          The old bean of the connector
   * @param newBean
   *          The new bean of the connector
   */
  protected void fireBeanChange(IPropertyChangeCapable oldBean,
      IPropertyChangeCapable newBean) {
    beanChangeSupport.fireBeanChange(oldBean, newBean);
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getChildConnector(String connectorKey) {
    if (THIS_PROPERTY.equals(connectorKey)) {
      return this;
    }
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
  @Override
  public BeanRefPropertyConnector clone(String newConnectorId) {
    BeanRefPropertyConnector clonedConnector = (BeanRefPropertyConnector) super
        .clone(newConnectorId);
    clonedConnector.beanChangeSupport = new BeanChangeSupport(clonedConnector);
    clonedConnector.childConnectors = new BeanConnectorMap(clonedConnector, beanConnectorFactory);
    clonedConnector.childConnectorSupport = new ChildConnectorSupport(
        clonedConnector);
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BeanRefPropertyConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  public void addChildConnector(@SuppressWarnings("unused")
  IValueConnector childConnector) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  public int getChildConnectorCount() {
    return getChildConnectorKeys().size();
  }
}
