/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.util.bean.IPropertyChangeCapable;

/**
 * This interface must be implemented by all model components in the application
 * domain. It establishes the minimal contract of a model component.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IComponent extends ILifecycleCapable, IPropertyChangeCapable {

  /**
   * Notifies its <code>PropertyChangeListener</code>s on a specific property
   * change.
   * 
   * @param property
   *          The property which changed.
   * @param oldValue
   *          The old value of the property.
   * @param newValue
   *          The new value of the property or <code>UNKNOWN</code>.
   */
  void firePropertyChange(String property, Object oldValue, Object newValue);

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
   * any other behaviour except a <code>PropertyChangeEvent</code>.
   * 
   * @param properties
   *          the properties to set.
   */
  void straightSetProperties(Map<String, Object> properties);

  /**
   * This method is used to update a persistent property without triggering any
   * other behaviour except a <code>PropertyChangeEvent</code>.
   * 
   * @param propertyName
   *          the name of the property to set.
   * @param backendPropertyValue
   *          the value to set the property with.
   */
  void straightSetProperty(String propertyName, Object backendPropertyValue);
}
