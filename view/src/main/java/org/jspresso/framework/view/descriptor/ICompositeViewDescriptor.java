/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor;

import java.util.List;

/**
 * This public interface is the super-interface of all view descriptors used as
 * containers for others.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICompositeViewDescriptor extends IViewDescriptor {

  /**
   * Gets wether this composite view is a master / detail view. When such a
   * master / detail composite view is built, each added child is bound to the
   * previous one in a master / detail relationship.
   * 
   * @return true if the child views are in a master / detail relationship.
   */
  boolean isCascadingModels();
  
  /**
   * Gets the child view descriptors.
   * 
   * @return the list of contained view descriptors.
   */
  List<IViewDescriptor> getChildViewDescriptors();

}
