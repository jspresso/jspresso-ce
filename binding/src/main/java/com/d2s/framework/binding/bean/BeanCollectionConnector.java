/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.bean;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

/**
 * A connector on an independant bean collection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanCollectionConnector extends BeanCollectionPropertyConnector {

  private Collection connecteeValue;

  /**
   * Constructs a new <code>BeanCollectionConnector</code> instance.
   * 
   * @param id
   *          the connector identifier.
   * @param elementClass
   *          the class of the elements (or superclass or interface) contained
   *          in this collection.
   * @param beanConnectorFactory
   *          the factory used to create the collection bean connectors.
   */
  public BeanCollectionConnector(String id, Class elementClass,
      IBeanConnectorFactory beanConnectorFactory) {
    super(id, elementClass, beanConnectorFactory);
  }

  /**
   * Returns the inner held collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return connecteeValue;
  }

  /**
   * Sets the inner held collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    Collection oldValue = connecteeValue;
    connecteeValue = (Collection) aValue;
    propertyChange(new PropertyChangeEvent(this, "connecteeValue",
        computeOldConnectorValue(oldValue), connecteeValue));
  }
}
