/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

/**
 * This is an interface used to identify classes responsible for providing
 * component accessors which are not directly related to the core properties.
 * Such classes provide derived (computed) properties of the component.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <T>
 *          The class of the extended entities.
 */
public interface IEntityExtension<T extends IEntity> {

  /**
   * Returns the entity instance to which this extension is attached.
   * 
   * @return The extended entity instance.
   */
  T getEntity();
}
