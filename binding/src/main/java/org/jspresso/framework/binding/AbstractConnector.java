/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding;

import org.jspresso.framework.util.bean.AbstractPropertyChangeCapable;

/**
 * This abstract class holds some default implementation for connector. All the
 * default connectors inherit from this default behaviour.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */

public abstract class AbstractConnector extends AbstractPropertyChangeCapable
    implements IConnector {

  private String id;

  /**
   * Constructs a new AbstractConnector using an identifier. In case of a bean
   * connector, this identifier must be the bean property the connector
   * connects.
   * 
   * @param id
   *            The connector identifier.
   */
  public AbstractConnector(String id) {
    this.id = id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  public AbstractConnector clone(String newConnectorId) {
    AbstractConnector clonedConnector = (AbstractConnector) super.clone();
    clonedConnector.id = newConnectorId;
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  public String getId() {
    return id;
  }

  /**
   * Changes the connector id.
   * 
   * @param id
   *            the connector identifier.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Default implementation does nothing.
   * <p>
   * {@inheritDoc}
   */
  public void updateState() {
    // Default empty implementation.
  }
}
