/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.d2s.framework.model.map.IMapComponentFactory;
import com.d2s.framework.util.accessor.IAccessorFactory;

/**
 * An action used in master/detail views where models are backed by maps to
 * create and add a new detail to a master domain object.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AddMapToMasterAction extends AbstractAddCollectionToMasterAction {

  private IMapComponentFactory mapComponentFactory;

  /**
   * Sets the mapComponentFactory.
   *
   * @param mapComponentFactory
   *          the mapComponentFactory to set.
   */
  public void setMapComponentFactory(IMapComponentFactory mapComponentFactory) {
    this.mapComponentFactory = mapComponentFactory;
  }

  /**
   * Returns the map accessor factory.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IAccessorFactory getAccessorFactory(Map<String, Object> context) {
    return getController(context).getMapAccessorFactory();
  }

  /**
   * Gets the new map component to add.
   *
   * @param context
   *          the action context.
   * @return the map to add to the collection.
   */
  @Override
  @SuppressWarnings("unchecked")
  protected List<?> getAddedComponents(@SuppressWarnings("unused")
  Map<String, Object> context) {
    Map<String, Object> newMap = mapComponentFactory
        .createMapComponentInstance(getModelDescriptor(context)
            .getCollectionDescriptor().getElementDescriptor(),
            getEntityFactory(context), getApplicationSession(context)
                .getPrincipal(), getApplicationSession(context));
    return Collections.singletonList(newMap);
  }
}
