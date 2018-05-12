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

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.security.auth.Subject;

import org.springframework.beans.BeanUtils;

import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.view.descriptor.IPropertyCardViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Describes a model-bound card view that is configurable with a model property used to determine the card yo display.
 *
 * @author Vincent Vandenschrick
 */
public class PropertyCardViewDescriptor extends AbstractCardViewDescriptor implements IPropertyCardViewDescriptor {

  private IAccessorFactory accessorFactory;
  private String           propertyName;

  /**
   * Gets property name.
   *
   * @return the property name
   */
  @Override
  public String getPropertyName() {
    return propertyName;
  }

  /**
   * Sets the model property name used to determine the card name.
   *
   * @param propertyName
   *     the property name
   */
  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  /**
   * Gets accessor factory.
   *
   * @return the accessor factory
   */
  public IAccessorFactory getAccessorFactory() {
    return accessorFactory;
  }

  /**
   * Sets accessor factory.
   *
   * @param accessorFactory
   *     the accessor factory
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * Delegates the card name selection to the model property.
   * <p>
   * {@inheritDoc}
   *
   * @param model
   *     the model
   * @param subject
   *     the subject
   * @return the card name for model
   */
  @Override
  public String getCardNameForModel(Object model, Subject subject) {
    if (model != null) {
      try {
        return getAccessorFactory().createPropertyAccessor(getPropertyName(), model.getClass()).getValue(model);
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
        throw new NestedRuntimeException(ex, "Unable to determine card name");
      }
    }
    return null;
  }

  /**
   * Registers the card views keyed by their name keys. The names used as key of
   * the {@code Map} must match the names that are returned by the
   * registered card name selector.
   *
   * @param cardViewDescriptors
   *     the cardViewDescriptors to set.
   */
  @Override
  public void setCardViewDescriptors(Map<String, IViewDescriptor> cardViewDescriptors) {
    super.setCardViewDescriptors(cardViewDescriptors);
  }

  /**
   * Clone read only basic card view descriptor.
   *
   * @return the basic card view descriptor
   */
  @Override
  public synchronized PropertyCardViewDescriptor cloneReadOnly() {
    if (readOnlyClone == null) {
      readOnlyClone = new PropertyCardViewDescriptor() {
        @Override
        public IViewDescriptor getCardViewDescriptor(String cardName) {
          IViewDescriptor cardViewDescriptor = super.getCardViewDescriptor(cardName);
          if (cardViewDescriptor == null) {
            IViewDescriptor delegate = PropertyCardViewDescriptor.this.getCardViewDescriptor(cardName);
            cardViewDescriptor = (IViewDescriptor) delegate.cloneReadOnly();
            putCardViewDescriptor(cardName, cardViewDescriptor);
          }
          return cardViewDescriptor;
        }

        @Override
        public String getCardNameForModel(Object model, Subject subject) {
          return PropertyCardViewDescriptor.this.getCardNameForModel(model, subject);
        }
      };
      BeanUtils.copyProperties(this, readOnlyClone);
    }
    return (PropertyCardViewDescriptor) readOnlyClone;
  }

}
