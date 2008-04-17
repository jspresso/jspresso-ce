/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor;

/**
 * This interface is the super-interface of all scalar properties descriptors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @see org.jspresso.framework.model.descriptor.IComponentDescriptor
 */
public interface IScalarPropertyDescriptor extends IPropertyDescriptor {

  /**
   * Gets the default initial value of this scalar property.
   * 
   * @return the default initial value of this scalar property.
   */
  Object getDefaultValue();
}
