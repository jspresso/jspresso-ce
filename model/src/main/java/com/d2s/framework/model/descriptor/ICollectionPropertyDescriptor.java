/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of collection properties.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICollectionPropertyDescriptor extends
    IRelationshipEndPropertyDescriptor, ICollectionDescriptorProvider {

  /**
   * Gets the descriptor of the collection referenced by this property.
   * 
   * @return the referenced collection descriptor.
   */
  ICollectionDescriptor getReferencedDescriptor();
}
