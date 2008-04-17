/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor;

import java.util.List;

/**
 * This public interface is implemented by "Tab" view descriptors. A typical
 * implementation of the described view could be a swing JTabPane.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ITabViewDescriptor extends ICompositeViewDescriptor {

  /**
   * Gets the list of view descriptors contained in this tab composite view.
   * Each sub view descriptor should be then presented using its name.
   * 
   * @return the list of contained view descriptors.
   */
  List<IViewDescriptor> getChildViewDescriptors();
}
