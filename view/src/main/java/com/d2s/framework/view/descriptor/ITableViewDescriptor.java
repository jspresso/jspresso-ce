/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

import java.util.List;

/**
 * This public interface is implemented by tabular view descriptors. For
 * instance, the described view can be a swing JTable presenting a collection of
 * java beans.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ITableViewDescriptor extends ICollectionViewDescriptor {

  /**
   * Gets the column view descriptors.
   * 
   * @return the column view descriptors.
   */
  List<ISubViewDescriptor> getColumnViewDescriptors();
}
