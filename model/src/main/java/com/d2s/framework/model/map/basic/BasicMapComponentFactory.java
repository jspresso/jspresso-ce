/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.map.basic;

import java.util.List;
import java.util.Map;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.model.entity.IEntityLifecycleHandler;
import com.d2s.framework.model.map.IMapComponentFactory;
import com.d2s.framework.model.service.ILifecycleInterceptor;
import com.d2s.framework.security.UserPrincipal;
import com.d2s.framework.util.collection.ObjectEqualityMap;

/**
 * Basic implementation of a IMapComponentFactory.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicMapComponentFactory implements IMapComponentFactory {

  /**
   * {@inheritDoc}
   */
  public Map<String, Object> createMapComponentInstance(
      IComponentDescriptor componentDescriptor, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler) {
    Map<String, Object> mapComponent = new ObjectEqualityMap<String, Object>();
    if (componentDescriptor.getLifecycleInterceptors() != null) {
      List<ILifecycleInterceptor> interceptors = componentDescriptor
          .getLifecycleInterceptors();
      if (interceptors != null) {
        for (ILifecycleInterceptor<Map<String, Object>> interceptor : interceptors) {
          interceptor.onCreate(mapComponent, entityFactory, principal,
              entityLifecycleHandler);
        }
      }
    }
    return mapComponent;
  }

}
