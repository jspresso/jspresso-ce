/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.gate;

import org.jspresso.framework.util.bean.AbstractPropertyChangeCapable;

/**
 * Base implementation of a gate.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
