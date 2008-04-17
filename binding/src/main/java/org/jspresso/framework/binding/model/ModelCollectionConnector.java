/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.model;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;


/**
 * A connector on an independent model collection.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
   * @param id
   *            the connector identifier.
   * @param modelDescriptor
   *            the model descriptor backing this connector.
   * @param modelConnectorFactory
   *            the factory used to create the collection model connectors.
   */
  ModelCollectionConnector(String id,
      ICollectionDescriptorProvider<?> modelDescriptor,
      IModelConnectorFactory modelConnectorFactory) {
    super(modelDescriptor, modelConnectorFactory);
    if (id != null) {
      setId(id);
    }
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
