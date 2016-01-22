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
package org.jspresso.framework.model.component;

import java.util.Map;

import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;

/**
 * This interface must be implemented by all model components in the application
 * domain. It establishes the minimal contract of a model component.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("EmptyMethod")
public interface IComponent extends IPropertyChangeCapable {

  /**
   * Gets the interface or class establishing the entity contract.
   *
   * @return the entity contract.
   */
  Class<? extends IComponent> getComponentContract();

  /**
   * Allows for temporary enabling/disabling property processors for internal
   * operations. This is useful for instance when cleaning relationships before
   * removing an entity. Calls to this method are typically enclosed in a
   * try/finally block to make sure that whatever the output is, the property
   * processors are re-enabled.
   *
   * @param enabled
   *          true if property processors should be enabled.
   */
  void setPropertyProcessorsEnabled(boolean enabled);

  /**
   * This method is used to get all the persistent properties without triggering
   * any other behaviour.
   *
   * @return the current properties values.
   */
  Map<String, Object> straightGetProperties();

  /**
   * This method is used to get a persistent property without triggering any
   * other behaviour.
   *
   * @param propertyName
   *          the name of the property to get.
   * @return the current value of the property.
   */
  Object straightGetProperty(String propertyName);

  /**
   * This method is used to update a persistent properties without triggering
   * any other behaviour except a {@code PropertyChangeEvent}.
   *
   * @param properties
   *          the properties to set.
   */
  void straightSetProperties(Map<String, Object> properties);

  /**
   * This method is used to update a persistent property without triggering any
   * other behaviour except a {@code PropertyChangeEvent}.
   *
   * @param propertyName
   *          the name of the property to set.
   * @param backendPropertyValue
   *          the value to set the property with.
   */
  void straightSetProperty(String propertyName, Object backendPropertyValue);

  /**
   * Gets the owning component or null.
   *
   * @return the owning component or null.
   */
  IComponent getOwningComponent();

  /**
   * Gets the owning property descriptor or null.
   *
   * @return the owning property descriptor or null.
   */
  IPropertyDescriptor getOwningPropertyDescriptor();

  /**
   * Sets the owning component or null.
   *
   * @param owningComponent
   *          the owning component or null.
   * @param owningPropertyDescriptor
   *          the owning property descriptor or null.
   */
  void setOwningComponent(IComponent owningComponent,
      IPropertyDescriptor owningPropertyDescriptor);

  /**
   * Check integrity. This method will trigger all property processors in order to detect violations of validation
   * rules.
   */
  void checkIntegrity();

  /**
   * Check mandatory properties. This method will check that all mandatory properties are actually filled.
   */
  void checkMandatoryProperties();
}
