/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view;

/**
 * A simple interface to implement an indirection on a view.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IViewProvider {

  /**
   * Get the referenced view.
   * 
   * @return the referenced view.
   */
  IView getView();
}
