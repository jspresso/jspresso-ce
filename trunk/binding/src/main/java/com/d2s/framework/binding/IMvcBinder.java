/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

/**
 * This is the interface which has to be implemented by classes which bind model
 * connectors to view connectors in a MVC relationship.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IMvcBinder {

  /**
   * Binds two connectors altogether.
   * 
   * @param viewConnector
   *            The connector for the view
   * @param modelConnector
   *            The connector for the model
   */
  void bind(IValueConnector viewConnector, IValueConnector modelConnector);
}
