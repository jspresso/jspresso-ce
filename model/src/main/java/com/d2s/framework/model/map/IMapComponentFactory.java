/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.map;

import java.util.Map;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.model.entity.IEntityLifecycleHandler;
import com.d2s.framework.security.UserPrincipal;

/**
 * This interface defines the contract of an map component factory.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IMapComponentFactory {

  /**
   * Creates a new component map based on the component descriptor.
   *
   * @param componentDescriptor
   *          the component descriptor the map must conform to.
   * @param entityFactory
   *          the entity factory that can be used in the interceprors.
   * @param principal
   *          the user principal that can be used in the interceprors.
   * @param entityLifecycleHandler
   *          the entity lifecycle handler that can be used in the interceprors.
   * @return the map component instance.
   */
  Map<String, Object> createMapComponentInstance(
      IComponentDescriptor componentDescriptor, IEntityFactory entityFactory,
      UserPrincipal principal, IEntityLifecycleHandler entityLifecycleHandler);
}
