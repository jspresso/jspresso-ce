/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

import java.util.List;

/**
 * This public interface is implemented by tabular view descriptors. For
 * instance, the described view can be a swing JTable presenting a collection of
 * java beans.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ITableViewDescriptor extends ICollectionViewDescriptor {

  /**
   * Gets the names of the underlying model properties which are made visible by
   * each column of the table.
   * 
   * @return the names of the underlying model rendered properties.
   */
  List<String> getRenderedProperties();
}
