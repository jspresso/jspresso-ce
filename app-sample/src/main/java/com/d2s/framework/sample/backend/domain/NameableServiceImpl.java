/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.sample.backend.domain;

import com.d2s.framework.model.service.IComponentService;
import com.d2s.framework.test.model.domain.Nameable;

/**
 * A very usefull ;) service to transform a <code>Nameable</code> name to
 * upper case.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class NameableServiceImpl implements IComponentService {

  /**
   * Transforms the name of an entity to uppercase.
   * 
   * @param entityToTransform
   *          The entity to make the name uppercase.
   */
  public void transformNameToUppercase(Nameable entityToTransform) {
    if (entityToTransform.getName() != null) {
      entityToTransform.setName(entityToTransform.getName().toUpperCase());
    }
  }
}
