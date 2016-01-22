/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.binding;

import org.jspresso.framework.model.IModelProvider;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.security.ISecurityHandlerAware;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.IValueChangeSource;
import org.jspresso.framework.util.exception.IExceptionHandler;
import org.jspresso.framework.util.gate.IGate;

/**
 * This public interface has to be implemented by any class which implements a
 * value connector. A value connector is a wrapper around a peer object. The
 * value connector : <li>keeps track of peer modifications (and fires
 * {@code IValueChangeEvent} accordingly). <li>is able to update peer value
 * <li>can attach to other connectors as {@code IValueChangeListener}
 *
 * @author Vincent Vandenschrick
 */
public interface IValueConnector extends IConnector, IValueChangeListener,
    Comparable<IValueConnector>, IValueChangeSource, ISecurityHandlerAware {

  /**
   * {@code READABLE_PROPERTY}.
   */
  String READABLE_PROPERTY        = "readable";

  /**
   * {@code WRITABLE_PROPERTY}.
   */
  String WRITABLE_PROPERTY        = "writable";

  /**
   * {@code MODEL_CONNECTOR_PROPERTY}.
   */
  String MODEL_CONNECTOR_PROPERTY = "modelConnector";

  /**
   * Adds a readability gate. Whenever one of the gate is not open, the
   * connector is not readable.
   *
   * @param gate
   *          the new gate to add.
   */
  void addReadabilityGate(IGate gate);

  /**
   * Removes all readability gates.
   */
  void resetReadabilityGates();

  /**
   * Adds a writability gate. Whenever one of the gate is not open, the
   * connector is not writable.
   *
   * @param gate
   *          the new gate to add.
   */
  void addWritabilityGate(IGate gate);

  /**
   * Removes all readability gates.
   */
  void resetWritabilityGates();

  /**
   * This method is called whenever this model connector has been bound to a
   * view connector.
   */
  void boundAsModel();

  /**
   * This method is called whenever this view connector binding has changed.
   */
  void boundAsView();

  /**
   * Cleans all bindings with view connectors.
   *
   * @param mvcBinder
   *          the mvcBinder used to unbind view connectors. It might be
   *          {@code null} for model connectors.
   */
  void recycle(IMvcBinder mvcBinder);

  /**
   * Clones this connector.
   *
   * @return the connector's clone.
   */
  @Override
  IValueConnector clone();

  /**
   * Clones this connector.
   *
   * @param newConnectorId
   *          the identifier of the clone connector
   * @return the connector's clone.
   */
  @Override
  IValueConnector clone(String newConnectorId);

  /**
   * Gets the value of the peer object.
   *
   * @param <T>
   *     type inference return.
   * @return The peer value
   */
  <T> T getConnectorValue();

  /**
   * Gets the connector this connector is attached to in mvc relationship.
   *
   * @return the model connector.
   */
  IValueConnector getModelConnector();

  /**
   * Gets the modelDescriptor.
   *
   * @return the modelDescriptor.
   */
  IModelDescriptor getModelDescriptor();

  /**
   * Gets the modelProvider.
   *
   * @return the modelProvider.
   */
  IModelProvider getModelProvider();

  /**
   * Gets the connector this connector is attached to in parent / child
   * relationship.
   *
   * @return the parent connector.
   */
  ICompositeValueConnector getParentConnector();

  /**
   * Tests whether the connector is readable.
   *
   * @return true if readable.
   */
  boolean isReadable();

  /**
   * Tests whether the connector is writable.
   *
   * @return true if writable.
   */
  boolean isWritable();

  /**
   * Called whenever readability may have changed.
   */
  void readabilityChange();

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
   * Sets a new value on the connectee and fire value change.
   *
   * @param aValue
   *          The value to set on the peer
   */
  void setConnectorValue(Object aValue);

  /**
   * Sets the exceptionHandler.
   *
   * @param exceptionHandler
   *          the exceptionHandler to set.
   */
  void setExceptionHandler(IExceptionHandler exceptionHandler);

  /**
   * Sets the connector locally readable. Calling this method does not guarantee
   * that the readability status of the connector will actually be changed. It
   * may have some other rules or accessibility interceptors that prevent this
   * to happen.
   *
   * @param locallyReadable
   *          true if readable.
   */
  void setLocallyReadable(boolean locallyReadable);

  /**
   * Sets the connector locally writable. Calling this method does not guarantee
   * that the writability status of the connector will actually be changed. It
   * may have some other rules or accessibility interceptors that prevent this
   * to happen.
   *
   * @param locallyWritable
   *          true if writable.
   */
  void setLocallyWritable(boolean locallyWritable);

  /**
   * Sets the connector this connector is attached to in mvc relationship.
   *
   * @param modelConnector
   *          the model connector.
   */
  void setModelConnector(IValueConnector modelConnector);

  /**
   * Sets the modelDescriptor.
   *
   * @param modelDescriptor
   *          the modelDescriptor.
   */
  void setModelDescriptor(IModelDescriptor modelDescriptor);

  /**
   * Sets the connector this connector is attached to in parent / child
   * relationship.
   *
   * @param parent
   *          the parent connector.
   */
  void setParentConnector(ICompositeValueConnector parent);

  /**
   * Called whenever writability may have changed.
   */
  void writabilityChange();
}
