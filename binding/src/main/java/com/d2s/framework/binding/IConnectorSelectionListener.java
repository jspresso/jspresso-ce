/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding;

/**
 * This interface is implemented by listeners willing to be notified of a
 * connector selection change on a connector selector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IConnectorSelectionListener {

  /**
   * This method is called whenever this listener is to be notified that the
   * selected connector has changed.
   * 
   * @param event
   *            the event containing the connector at the origin of the event
   *            and the selected connector inside it.
   */
  void selectedConnectorChange(ConnectorSelectionEvent event);
}
