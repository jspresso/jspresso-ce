/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;

/**
 * Factory for list-of-value views.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
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
