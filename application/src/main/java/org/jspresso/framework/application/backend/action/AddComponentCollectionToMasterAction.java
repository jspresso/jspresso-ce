/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.application.backend.action;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.entity.IEntity;

/**
 * An action used in master/detail views to create and add a new detail to a
 * master domain object. Entity (or component) to add are actually created by
 * the entity factory retrieved from the action context. The class of entity
 * (component) to create is either taken :
 * <ol>
 * <li>from the action context under the key {@code ELEMENT_DESCRIPTOR}</li>
 * <li>or, if it does not exist, taken from the view model descriptor. In this
 * case, the component descriptor to use is the element descriptor of the
 * underlying collection descriptor.</li>
 * </ol>
 *
 * @author Vincent Vandenschrick
 */
public class AddComponentCollectionToMasterAction extends
    AbstractAddCollectionToMasterAction {

  /**
   * The the descriptor of the model collection element domain object the action
   * was triggered on.
   */
  public static final String ELEMENT_DESCRIPTOR = "ELEMENT_DESCRIPTOR";

  /**
   * Gets the new entity to add. It is created using the information contained
   * in the context.
   *
   * @param context
   *          the action context.
   * @return the entity to add to the collection.
   */
  @SuppressWarnings("unchecked")
  @Override
  protected List<?> getAddedComponents(Map<String, Object> context) {
    IComponentDescriptor<?> elementDescriptor = getAddedElementDescriptor(context);
    IComponent newElement;
    if (elementDescriptor.isEntity()) {
      newElement = getEntityFactory(context).createEntityInstance(
          (Class<? extends IEntity>) elementDescriptor.getComponentContract());
    } else {
      newElement = getEntityFactory(context).createComponentInstance(
          (Class<? extends IComponent>) elementDescriptor
              .getComponentContract());
    }
    return Collections.singletonList(newElement);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IComponentDescriptor<?> getAddedElementDescriptor(Map<String, Object> context) {
    IComponentDescriptor<?> elementDescriptor = (IComponentDescriptor<?>) context
        .get(ELEMENT_DESCRIPTOR);
    if (elementDescriptor == null) {
      elementDescriptor = super.getAddedElementDescriptor(context);
    }
    return elementDescriptor;
  }
}
