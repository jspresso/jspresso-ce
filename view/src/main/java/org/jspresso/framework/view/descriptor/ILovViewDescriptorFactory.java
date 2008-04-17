/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor;

import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;

/**
 * Factory for list-of-value views.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ILovViewDescriptorFactory {

  /**
   * Creates a new lov view descriptor for a component descriptor.
   * 
   * @param entityRefDescriptor
   *            the entity reference descriptor.
   * @return the created view descriptor.
   */
  IViewDescriptor createLovViewDescriptor(
      IReferencePropertyDescriptor<?> entityRefDescriptor);
}
