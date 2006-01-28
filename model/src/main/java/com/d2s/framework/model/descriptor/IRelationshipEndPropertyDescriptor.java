/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * This is the super-interface of all inter-component relationship descriptors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IRelationshipEndPropertyDescriptor extends IPropertyDescriptor {

  /**
   * Gets the reverse relationship descriptor of the underlying relation.
   * 
   * @return value
   */
  IRelationshipEndPropertyDescriptor getReverseRelationEnd();

}
