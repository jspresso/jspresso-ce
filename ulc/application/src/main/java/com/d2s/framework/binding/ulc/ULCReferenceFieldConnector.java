/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding.ulc;

import java.util.Collection;
import java.util.Collections;

import org.jspresso.framework.binding.ConnectorValueChangeEvent;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConnectorValueChangeListener;
import org.jspresso.framework.binding.IValueConnector;

import com.d2s.framework.gui.ulc.components.server.ULCActionField;

/**
 * ULCReferenceFieldConnector connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCReferenceFieldConnector extends ULCActionFieldConnector
    implements ICompositeValueConnector {

  private IConnectorValueChangeListener toStringListener;
  private IValueConnector               toStringPropertyConnector;

  /**
   * Constructs a new <code>ULCReferenceFieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param actionField
   *            the connected ULCActionField.
   */
  public ULCReferenceFieldConnector(String id, ULCActionField actionField) {
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
  public ULCReferenceFieldConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ULCReferenceFieldConnector clone(String newConnectorId) {
    ULCReferenceFieldConnector clonedConnector = (ULCReferenceFieldConnector) super
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
   *            the toStringPropertyConnector to set.
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
}
