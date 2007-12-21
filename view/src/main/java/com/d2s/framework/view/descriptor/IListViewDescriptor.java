/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

/**
 * This public interface is implemented by list view descriptors. The described
 * view will typically be implemented by a swing JList representing a collection
 * of java beans described by one of their property.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IListViewDescriptor extends ICollectionViewDescriptor {

  /**
   * Gets the name of the underlying model propertiy which is made visible by
   * the list.
   * 
   * @return the name of the underlying model rendered property.
   */
  String getRenderedProperty();
}
