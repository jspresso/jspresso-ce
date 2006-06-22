/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.bean;

import com.d2s.framework.binding.ConnectorMap;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.util.bean.PropertyHelper;

/**
 * Serves as an auto-generating ConnectorMap for beans. It may be used to hold a
 * bean model in a MVC pattern.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanConnectorMap extends ConnectorMap {

  private IBeanConnectorFactory beanConnectorFactory;

  /**
   * Constructs a new instance based on the bean class passed as parameter.
   * 
   * @param parentConnector
   *          the bean connector holding the connector map.
   * @param beanConnectorFactory
   *          the factory used to create the bean connectors.
   */
  BeanConnectorMap(BeanRefPropertyConnector parentConnector,
      IBeanConnectorFactory beanConnectorFactory) {
    super(parentConnector);
    this.beanConnectorFactory = beanConnectorFactory;
  }

  /**
   * Will throw an <code>UnsupportedOperationException</code>. This is a
   * self-generating connector map.
   * <p>
   * {@inheritDoc}
   * 
   * @see com.d2s.framework.binding.IConnectorMap#getConnector(String)
   */
  @Override
  public void addConnector(@SuppressWarnings("unused")
  String storageKey, @SuppressWarnings("unused")
  IValueConnector connector) {
    throw new UnsupportedOperationException();
  }

  /**
   * This method implements connector auto creation. If a connector with the
   * <code>connectorId</code> doesn't already exist, a new one is created
   * using the <code>IBeanConnectorFactory</code> and register it as
   * <code>IBeanChangeListener</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IValueConnector getConnector(String connectorId) {
    BeanPropertyConnector connector = (BeanPropertyConnector) super
        .getConnector(connectorId);
    if (connector == null) {
      Class propertyType = PropertyHelper.getPropertyType(getParentConnector().getBeanClass(), connectorId);
      connector = beanConnectorFactory.createBeanPropertyConnector(connectorId,
          propertyType);
      super.addConnector(connector.getId(), connector);
    }
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected BeanRefPropertyConnector getParentConnector() {
    return (BeanRefPropertyConnector) super.getParentConnector();
  }
}
