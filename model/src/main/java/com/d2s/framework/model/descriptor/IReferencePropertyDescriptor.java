/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of reference properties.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IReferencePropertyDescriptor extends
    IRelationshipEndPropertyDescriptor {

  /**
   * Gets the descriptor of the component referenced by this property.
   * 
   * @return the referenced component descriptor
   */
  IComponentDescriptor getReferencedDescriptor();

}
