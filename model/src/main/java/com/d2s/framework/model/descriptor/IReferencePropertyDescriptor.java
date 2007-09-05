/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

import java.util.Map;

/**
 * This interface is implemented by descriptors of reference properties.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the concrete component type.
 */
public interface IReferencePropertyDescriptor<E> extends
    IRelationshipEndPropertyDescriptor, IComponentDescriptorProvider<E> {

  /**
   * Gets the initialization map between master object attributes and query
   * entity for LOV. For instance, a mapping holding (attrA,attrB) will indicate
   * that the lov query entity should be initialized with its 'attrA' value
   * initialized with the 'attrB' value of its master.
   * 
   * @return the initialisation mapping.
   */
  Map<String, String> getInitializationMapping();

  /**
   * Gets the descriptor of the component referenced by this property.
   * 
   * @return the referenced component descriptor
   */
  IComponentDescriptor<E> getReferencedDescriptor();

}
