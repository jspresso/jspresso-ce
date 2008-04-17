/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor;

/**
 * This interface is implemented by objects which might be interested by an
 * underlying model descriptor..
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModelDescriptorAware {

  /**
   * Sets the underlying model descriptor.
   * 
   * @param modelDescriptor
   *            the underlying model descriptor.
   */
  void setModelDescriptor(IModelDescriptor modelDescriptor);
}
