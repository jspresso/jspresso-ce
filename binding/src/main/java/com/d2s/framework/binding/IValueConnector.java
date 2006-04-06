/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.beans.PropertyChangeListener;

import com.d2s.framework.util.IGate;

/**
 * This public interface has to be implemented by any class which implements a
 * value connector. A value connector is a wrapper around a peer object. The
 * value connector :
 * <li>keeps track of peer modifications (and fires
 * <code>IConnectorValueChangeEvent</code> accordingly).
 * <li>is able to update peer value
 * <li>can attach to other connectors as
 * <code>IConnectorValueChangeListener</code>
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IValueConnector extends IConnector,
    IConnectorValueChangeListener, Comparable<IValueConnector> {

  /**
   * <code>READABLE_PROPERTY</code>.
   */
  String READABLE_PROPERTY = "readable";

  /**
   * <code>WRITABLE_PROPERTY</code>.
   */
  String WRITABLE_PROPERTY = "writable";

  /**
   * Gets the value of the peer object.
   * 
   * @return The peer value
   */
  Object getConnectorValue();

  /**
   * Sets a new value on the connectee and fire value change.
   * 
   * @param aValue
   *          The value to set on the peer
   */
  void setConnectorValue(Object aValue);

  /**
   * Adds a new Connector listener to this connector.
   * 
   * @param listener
   *          The added listener
   */
  void addConnectorValueChangeListener(IConnectorValueChangeListener listener);

  /**
   * Removes a Connector listener from this connector.
   * 
   * @param listener
   *          The removed listener
   */
  void removeConnectorValueChangeListener(IConnectorValueChangeListener listener);

  /**
   * Clones this connector.
   * 
   * @param newConnectorId
   *          the identifier of the clone connector
   * @return the connector's clone.
   */
  IValueConnector clone(String newConnectorId);

  /**
   * Clones this connector.
   * 
   * @return the connector's clone.
   */
  IValueConnector clone();

  /**
   * Gets the connector this connector is attached to in parent / child
   * relationship.
   * 
   * @return the parent connector.
   */
  ICompositeValueConnector getParentConnector();

  /**
   * Sets the connector this connector is attached to in parent / child
   * relationship.
   * 
   * @param parent
   *          the parent connector.
   */
  void setParentConnector(ICompositeValueConnector parent);

  /**
   * Forces the different events to be fired towards the listeners passed as
   * parameter.
   * 
   * @param modelConnectorListener
   *          the connector value change listener.
   * @param readChangeListener
   *          the read change listener.
   * @param writeChangeListener
   *          the write change listener.
   */
  void boundAsModel(IConnectorValueChangeListener modelConnectorListener,
      PropertyChangeListener readChangeListener,
      PropertyChangeListener writeChangeListener);

  /**
   * This method is called whenever this view connector binding has changed.
   */
  void boundAsView();

  /**
   * Is the connector readable ?
   * 
   * @return true if readable.
   */
  boolean isReadable();

  /**
   * Is the connector writable ?
   * 
   * @return true if writable.
   */
  boolean isWritable();

  /**
   * Sets the connector locally readable. Calling this method does not garantee
   * that the readability status of the connector will actually be changed. It
   * may have some other rules or accessibility interceptors that prevent this
   * to happen.
   * 
   * @param locallyReadable
   *          true if readable.
   */
  void setLocallyReadable(boolean locallyReadable);

  /**
   * Sets the connector locally writable. Calling this method does not garantee
   * that the writability status of the connector will actually be changed. It
   * may have some other rules or accessibility interceptors that prevent this
   * to happen.
   * 
   * @param locallyWritable
   *          true if writable.
   */
  void setLocallyWritable(boolean locallyWritable);

  /**
   * Adds a readability gate. Whenever one of the gate is not open, the
   * connector is not readable.
   * 
   * @param gate
   *          the new gate to add.
   */
  void addReadabilityGate(IGate gate);

  /**
   * Adds a writability gate. Whenever one of the gate is not open, the
   * connector is not writable.
   * 
   * @param gate
   *          the new gate to add.
   */
  void addWritabilityGate(IGate gate);

  /**
   * Removes a readability gate.
   * 
   * @param gate
   *          the new gate to remove.
   */
  void removeReadabilityGate(IGate gate);

  /**
   * Removes a writability gate.
   * 
   * @param gate
   *          the new gate to remove.
   */
  void removeWritabilityGate(IGate gate);

  /**
   * Gets the connector this connector is attached to in mvc relationship.
   * 
   * @return the model connector.
   */
  IValueConnector getModelConnector();

  /**
   * Sets the connector this connector is attached to in mvc relationship.
   * 
   * @param modelConnector
   *          the model connector.
   */
  void setModelConnector(IValueConnector modelConnector);
}
