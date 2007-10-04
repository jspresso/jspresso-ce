/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util;

/**
 * This interface is implemented by classes which provide a mapping between two
 * sets of indices (identified as model and view index sets).
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IIndexMapper {

  /**
   * View to model index translation.
   * 
   * @param viewIndex
   *            the view index to translate.
   * @return the resulting model index.
   */
  int modelIndex(int viewIndex);

  /**
   * Model to view index translation.
   * 
   * @param modelIndex
   *            the model index to translate.
   * @return the resulting view index.
   */
  int viewIndex(int modelIndex);

}
