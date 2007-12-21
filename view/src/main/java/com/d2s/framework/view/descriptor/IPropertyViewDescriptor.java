/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

import java.util.List;

/**
 * This public interface is implemented by view descriptors which are just
 * presenting a property.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IPropertyViewDescriptor extends IViewDescriptor {

  /**
   * Gets the child properties to display in case of a complex property.
   * 
   * @return The list of displayed properties in the case of a complex property.
   */
  List<String> getRenderedChildProperties();
}
