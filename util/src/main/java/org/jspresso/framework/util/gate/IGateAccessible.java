/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.gate;

import java.util.Collection;

/**
 * Accessible using gates
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IGateAccessible {

  /**
   * Gets the collection of gates determining the readability state.
   * 
   * @return the collection of gates determining the readability state.
   */
  Collection<IGate> getReadabilityGates();

  /**
   * Gets the collection of gates determining the writability state.
   * 
   * @return the collection of gates determining the writability state.
   */
  Collection<IGate> getWritabilityGates();

  /**
   * Wether read-only is forced.
   * 
   * @return true if read-only.
   */
  boolean isReadOnly();
}
