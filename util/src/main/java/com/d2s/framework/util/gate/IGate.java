/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.gate;

import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * A simple open / close, true / false interface.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IGate extends IPropertyChangeCapable, Cloneable {

  /**
   * <code>OPEN_PROPERTY</code>.
   */
  String OPEN_PROPERTY = "open";

  /**
   * Clones the gate.
   * 
   * @return the cloned gate.
   */
  IGate clone();

  /**
   * Is the gate open ?
   * 
   * @return true if open.
   */
  boolean isOpen();
}
