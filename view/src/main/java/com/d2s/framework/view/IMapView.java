/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view;

/**
 * This interface establishes the general contract of a composite view which has
 * child views which are keyed by a string.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 */
public interface IMapView<E> extends ICompositeView<E> {

  /**
   * Gets the child view indexed by the key or null if the key does not exist.
   * 
   * @param key
   *            the key to llok up the child view.
   * @return the child view or null if none.
   */
  IView<E> getChild(String key);
}
