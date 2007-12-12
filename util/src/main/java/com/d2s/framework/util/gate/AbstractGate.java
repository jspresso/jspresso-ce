/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.gate;

import com.d2s.framework.util.bean.AbstractPropertyChangeCapable;

/**
 * Base implementation of a gate.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractGate extends AbstractPropertyChangeCapable
    implements IGate {

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractGate clone() {
    AbstractGate clonedGate = (AbstractGate) super.clone();
    return clonedGate;
  }
}
