/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

/**
 * This public interface is implemented by container view descriptors which are
 * just nesting a child view. This type of view is useful to build nested
 * property views.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface INestingViewDescriptor extends IViewDescriptor {

  /**
   * Gets the nested contained view descriptor.
   * 
   * @return the nested contained view descriptor.
   */
  IViewDescriptor getNestedViewDescriptor();
}
