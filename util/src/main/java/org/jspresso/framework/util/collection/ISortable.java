/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.collection;

import java.util.Map;

/**
 * Defines the contract for sortable bean collections.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ISortable {

  /**
   * Gets the ordered list of sorting attributes along with their individual
   * sorting direction.
   * 
   * @return the ordered list of sorting attributes along with their individual
   *         sorting direction.
   */
  Map<String, ESort> getOrderingProperties();

  /**
   * Sets the ordered list of sorting attributes along with their individual
   * sorting direction.
   * 
   * @param orderingProperties
   *          the ordered list of sorting attributes along with their individual
   *          sorting direction.
   */
  void setOrderingProperties(Map<String, ESort> orderingProperties);
}
