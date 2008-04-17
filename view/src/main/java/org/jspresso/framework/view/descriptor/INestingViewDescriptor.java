/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor;

/**
 * This public interface is implemented by container view descriptors which are
 * just nesting a child view. This type of view is useful to build nested
 * property views.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
