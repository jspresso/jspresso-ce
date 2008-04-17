/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor;

import org.jspresso.framework.util.descriptor.IDescriptor;

/**
 * This is just a marker interface for model descriptors (ususally bean
 * descriptors and sub descriptors).
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModelDescriptor extends IDescriptor {

  /**
   * Gets the type of the model.
   * 
   * @return the type of the model.
   */
  Class<?> getModelType();
}
