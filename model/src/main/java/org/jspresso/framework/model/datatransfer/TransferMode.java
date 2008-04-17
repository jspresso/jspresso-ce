/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.datatransfer;

/**
 * This enumeration defines all the supported transfer types.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
