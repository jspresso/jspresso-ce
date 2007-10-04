/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.datatransfer;

/**
 * This enumeration defines all the supported transfer types.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public enum TransferMode {

  /**
   * <code>COPY</code> means that the original object is kept as is.
   */
  COPY,

  /**
   * <code>MOVE</code> means that the original object is moved to the
   * destination.
   */
  MOVE
}
