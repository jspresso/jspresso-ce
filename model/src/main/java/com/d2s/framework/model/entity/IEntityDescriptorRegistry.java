/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;

/**
 * A registry mapping the entity contracts with their descriptors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntityDescriptorRegistry {

  /**
   * Retrieves an entity descriptor from its contract.
   * 
   * @param <T>
   *          the type of the entity.
   * @param entityContract
   *          the entity contract.
   * @return th entity descriptor.
   */
  <T extends IEntity> IEntityDescriptor getEntityDescriptor(
      Class<T> entityContract);
}
