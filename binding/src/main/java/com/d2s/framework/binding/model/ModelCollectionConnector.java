/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

import com.d2s.framework.model.descriptor.ICollectionDescriptorProvider;

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

  private Collection<?> connecteeValue;

  /**
   * Constructs a new <code>ModelCollectionConnector</code> instance.
   * 
   * @param modelDescriptor
   *            the model descriptor backing this connector.
   * @param modelConnectorFactory
   *            the factory used to create the collection model connectors.
   */
  public ModelCollectionConnector(
      ICollectionDescriptorProvider<?> modelDescriptor,
      IModelConnectorFactory modelConnectorFactory) {
    super(modelDescriptor, modelConnectorFactory);
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
    Collection<?> oldValue = connecteeValue;
    connecteeValue = (Collection<?>) aValue;
    propertyChange(new PropertyChangeEvent(this, "connecteeValue",
        computeOldConnectorValue(oldValue), connecteeValue));
  }
}
