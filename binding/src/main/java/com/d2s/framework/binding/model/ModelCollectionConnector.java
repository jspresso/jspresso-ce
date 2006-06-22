/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import java.util.Collection;

/**
 * A connector on an independent model collection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModelCollectionConnector extends ModelCollectionPropertyConnector {

  private Collection connecteeValue;

  /**
   * Constructs a new <code>ModelCollectionConnector</code> instance.
   * 
   * @param id
   *          the connector identifier.
   * @param elementClass
   *          the collection element class.
   * @param modelConnectorFactory
   *          the factory used to create the collection model connectors.
   */
  public ModelCollectionConnector(String id, Class elementClass,
      IModelConnectorFactory modelConnectorFactory) {
    super(id, modelConnectorFactory);
    setElementClass(elementClass);
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
  protected void setConnecteeValue(@SuppressWarnings("unused")
  Object aValue) {
    // TODO complete bean connector.
    // Collection oldValue = connecteeValue;
    // connecteeValue = (Collection) aValue;
    // propertyChange(new PropertyChangeEvent(this, "connecteeValue",
    // computeOldConnectorValue(oldValue), connecteeValue));
  }
}
