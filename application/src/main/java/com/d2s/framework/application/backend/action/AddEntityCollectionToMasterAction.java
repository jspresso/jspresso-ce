/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.entity.IEntity;

/**
 * An action used in master/detail views to create and add a new detail to a
 * master domain object.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AddEntityCollectionToMasterAction extends AbstractAddCollectionToMasterAction {

  /**
   * Gets the new entity to add. It is created using the informations contained
   * in the context.
   *
   * @param context
   *          the action context.
   * @return the entity to add to the collection.
   */
  @Override
  @SuppressWarnings("unchecked")
  protected List<?> getAddedComponents(Map<String, Object> context) {
    IComponentDescriptor elementDescriptor = (IComponentDescriptor) context
        .get(ActionContextConstants.ELEMENT_DESCRIPTOR);
    if (elementDescriptor == null) {
      elementDescriptor = ((ICollectionPropertyDescriptor) getModelDescriptor(context))
          .getReferencedDescriptor().getElementDescriptor();
    }

    IEntity newEntity = getEntityFactory(context).createEntityInstance(
        (Class<? extends IEntity>) elementDescriptor.getComponentContract());
    return Collections.singletonList(newEntity);
  }
}
