/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view;

import java.util.List;

/**
 * This interface establishes the general contract of a composite view which has
 * child views.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 */
public interface ICompositeView<E> extends IView<E> {

  /**
   * Gets the child views.
   * 
   * @return the child views.
   */
  List<IView<E>> getChildren();
}
