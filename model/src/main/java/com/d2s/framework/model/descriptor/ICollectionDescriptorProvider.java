/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * Implementing classes are able to provide a collection descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the concrete collection component type.
 */
public interface ICollectionDescriptorProvider<E> extends IModelDescriptor {

  /**
   * Gets the collection descriptor.
   * 
   * @return the collection descriptor.
   */
  ICollectionDescriptor<E> getCollectionDescriptor();
}
