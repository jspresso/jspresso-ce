/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;

/**
 * Factory for query component view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision: 959 $
 * @author Vincent Vandenschrick
 */
public interface IQueryViewDescriptorFactory {

  /**
   * Creates a new query component view descriptor.
   * 
   * @param queryComponentDescriptor
   *            the query component descriptor.
   * @return the created view descriptor.
   */
  IViewDescriptor createQueryViewDescriptor(
      IComponentDescriptor<Object> queryComponentDescriptor);
}
