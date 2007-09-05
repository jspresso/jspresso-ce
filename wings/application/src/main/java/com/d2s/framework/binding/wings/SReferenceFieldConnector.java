/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.wings;

import java.util.Collection;
import java.util.Collections;

import com.d2s.framework.binding.ConnectorValueChangeEvent;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IConnectorValueChangeListener;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.gui.wings.components.SActionField;

/**
 * SReferenceFieldConnector connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SReferenceFieldConnector extends SActionFieldConnector implements
    ICompositeValueConnector {

  private final class ToStringConnectorListener implements
      IConnectorValueChangeListener {

    /**
     * {@inheritDoc}
     */
    public void connectorValueChange(@SuppressWarnings("unused")
    ConnectorValueChangeEvent evt) {
      setConnecteeValue(getConnecteeValue());
    }

  }
  private IConnectorValueChangeListener toStringListener;

  private IValueConnector               toStringPropertyConnector;

  /**
   * Constructs a new <code>SActionFieldConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param actionField
   *          the connected SActionField.
   */
  public SReferenceFieldConnector(String id, SActionField actionField) {
    super(id, actionField);
    toStringListener = new ToStringConnectorListener();
  }

  /**
   * {@inheritDoc}
   */
  public void addChildConnector(@SuppressWarnings("unused")
  IValueConnector childConnector) {
    throw new UnsupportedOperationException(
        "Child connectors cannot be added to action field connector");
  }

  /**
   * {@inheritDoc}
   */
  public boolean areChildrenReadable() {
    return isReadable();
  }

  /**
   * {@inheritDoc}
   */
  public boolean areChildrenWritable() {
    return isWritable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SReferenceFieldConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SReferenceFieldConnector clone(String newConnectorId) {
    SReferenceFieldConnector clonedConnector = (SReferenceFieldConnector) super
        .clone(newConnectorId);
    if (toStringPropertyConnector != null) {
      clonedConnector.toStringPropertyConnector = toStringPropertyConnector
          .clone();
    }
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getChildConnector(String connectorKey) {
    if (connectorKey.equals(toStringPropertyConnector.getId())) {
      return toStringPropertyConnector;
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public int getChildConnectorCount() {
    if (toStringPropertyConnector != null) {
      return 1;
    }
    return 0;
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getChildConnectorKeys() {
    if (toStringPropertyConnector != null) {
      return Collections.singleton(toStringPropertyConnector.getId());
    }
    return null;
  }

  /**
   * Sets the toStringPropertyConnector.
   * 
   * @param toStringPropertyConnector
   *          the toStringPropertyConnector to set.
   */
  public void setToStringPropertyConnector(
      IValueConnector toStringPropertyConnector) {
    if (this.toStringPropertyConnector != null) {
      this.toStringPropertyConnector
          .removeConnectorValueChangeListener(toStringListener);
    }
    this.toStringPropertyConnector = toStringPropertyConnector;
    if (this.toStringPropertyConnector != null) {
      this.toStringPropertyConnector
          .addConnectorValueChangeListener(toStringListener);
    }
  }
}
