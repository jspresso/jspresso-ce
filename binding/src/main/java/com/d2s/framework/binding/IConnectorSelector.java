/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

/**
 * This interface is implemented by any structure capable of selecting a
 * connector (a view collection connector is such a structure since it keeps
 * track of its children selection).
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IConnectorSelector {

  /**
   * Adds a connector selection listener to this connector selector.
   * 
   * @param listener
   *          the listener to add.
   */
  void addConnectorSelectionListener(IConnectorSelectionListener listener);

  /**
   * Removes a connector selection listener to this connector selector.
   * 
   * @param listener
   *          the listener to remove.
   */
  void removeConnectorSelectionListener(IConnectorSelectionListener listener);

  /**
   * Setes whether this connector selector should only forward its selection (a
   * table connector for instance) or also its children selections (a tree
   * connector for instance).
   * 
   * @param tracksChildrenSelection
   *          true if the connector selector should also forward its children
   *          selections.
   */
  void setTracksChildrenSelection(boolean tracksChildrenSelection);

  /**
   * Triggers notification of a connector selection event. This method has to be
   * made public to cope with notification of the children selection events.
   * 
   * @param evt
   *          the event to be propagated.
   */
  void fireSelectedConnectorChange(ConnectorSelectionEvent evt);
}
