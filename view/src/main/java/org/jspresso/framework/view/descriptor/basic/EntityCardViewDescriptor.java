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
package org.jspresso.framework.view.descriptor.basic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.view.ViewException;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This card view provides a simple card determination strategy that is based on
 * the bound model type. This strategy pulls up the card whose model descriptor
 * matches the type of the bound model.
 *
 * @author Vincent Vandenschrick
 */
public class EntityCardViewDescriptor extends AbstractCardViewDescriptor {

  private static final Logger LOG = LoggerFactory
                                      .getLogger(EntityCardViewDescriptor.class);

  private List<Class<?>>      registeredTypes;

  /**
   * Uses the component contract name as card name.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getCardNameForModel(Object model, Subject subject) {
    if (model != null) {
      if (model instanceof IComponent) {
        return ((IComponent) model).getComponentContract().getName();
      }
      return model.getClass().getName();
    }
    return null;
  }

  /**
   * Registers the list of card view descriptors. Every time the bound model
   * changes, this list is iterated until a card with a matching model is found.
   * The first matching card is displayed. Whenever no registered card matches,
   * an empty view is displayed.
   *
   * @param viewDescriptors
   *          the viewDescriptors to set.
   */
  public void setViewDescriptors(List<IViewDescriptor> viewDescriptors) {
    Map<String, IViewDescriptor> classCardMapping = new LinkedHashMap<>();
    registeredTypes = new ArrayList<>();
    for (IViewDescriptor componentViewDescriptor : viewDescriptors) {
      if (!(componentViewDescriptor.getModelDescriptor() instanceof IComponentDescriptor<?>)) {
        throw new ViewException(
            "Entity card view does not support cards without model"
                + " descriptor or with a model descriptor that is not a component descriptor.");
      }
      Class<?> componentContract = ((IComponentDescriptor<?>) componentViewDescriptor
          .getModelDescriptor()).getComponentContract();
      registeredTypes.add(componentContract);
      classCardMapping
          .put(componentContract.getName(), componentViewDescriptor);
    }
    setCardViewDescriptors(classCardMapping);
  }

  /**
   * We have to take care of cards that have been registered through a
   * superclass or interface of the model entity.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getCardViewDescriptor(String cardName) {
    Class<?> modelType = null;
    try {
      modelType = Class.forName(cardName);
    } catch (ClassNotFoundException ex) {
      LOG.warn("Unsupported entity card name {}", cardName);
    }
    if (modelType != null) {
      for (Class<?> registeredType : registeredTypes) {
        if (registeredType.isAssignableFrom(modelType)) {
          return super.getCardViewDescriptor(registeredType.getName());
        }
      }
    }
    return super.getCardViewDescriptor(cardName);
  }
}
