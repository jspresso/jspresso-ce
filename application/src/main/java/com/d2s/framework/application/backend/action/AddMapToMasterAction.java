/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.d2s.framework.model.component.service.ILifecycleInterceptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.util.accessor.IAccessorFactory;
import com.d2s.framework.util.collection.ObjectEqualityMap;

/**
 * An action used in master/detail views where models are backed by maps to
 * create and add a new detail to a master domain object.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AddMapToMasterAction extends AbstractAddCollectionToMasterAction {

  /**
   * Returns the map accessor factory.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IAccessorFactory getBeanAccessorFactory(Map<String, Object> context) {
    return getController(context).getMapAccessorFactory();
  }

  /**
   * Gets the new map component to add.
   * 
   * @param context
   *            the action context.
   * @return the map to add to the collection.
   */
  @Override
  @SuppressWarnings("unchecked")
  protected List<?> getAddedComponents(@SuppressWarnings("unused")
  Map<String, Object> context) {
    IComponentDescriptor componentDescriptor = getModelDescriptor(context)
        .getCollectionDescriptor().getElementDescriptor();
    Map<String, Object> newMap = new ObjectEqualityMap<String, Object>();
    if (componentDescriptor.getLifecycleInterceptors() != null) {
      List<ILifecycleInterceptor> interceptors = componentDescriptor
          .getLifecycleInterceptors();
      if (interceptors != null) {
        for (ILifecycleInterceptor<Map<String, Object>> interceptor : interceptors) {
          interceptor.onCreate(newMap, getEntityFactory(context),
              getApplicationSession(context).getPrincipal(),
              getApplicationSession(context));
        }
      }
    }
    return Collections.singletonList(newMap);
  }
}
