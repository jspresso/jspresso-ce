/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.entity;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.entity.IEntity;

/**
 * This interface is implemented by descriptors of entities (java bean style).
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntityDescriptor extends IComponentDescriptor {

  /**
   * Gets the interface class defining the component contract.
   * 
   * @return the interface class defining the component contract.
   */
  Class<? extends IEntity> getComponentContract();
}
