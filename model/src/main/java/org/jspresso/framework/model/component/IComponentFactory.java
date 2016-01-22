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

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorRegistry;
import org.jspresso.framework.util.accessor.IAccessorFactory;

/**
 * This interface defines the contract of a component factory.
 *
 * @author Vincent Vandenschrick
 */
public interface IComponentFactory extends IComponentDescriptorRegistry {

  /**
   * Creates a new component instance based on the component descriptor.
   *
   * @param <T>
   *          the concrete class of the created component.
   * @param componentContract
   *          the class of the component to create.
   * @return the component instance.
   */
  <T extends IComponent> T createComponentInstance(Class<T> componentContract);

  /**
   * Creates a new component instance based on the component descriptor. All
   * method calls are handled by the component delegate.
   *
   * @param <T>
   *          the concrete class of the created component.
   * @param componentContract
   *          the class of the component to create.
   * @param delegate
   *          the component delegate instance.
   * @return the component instance.
   */
  <T extends IComponent> T createComponentInstance(Class<T> componentContract,
      Object delegate);

  /**
   * Creates a new query component instance based on the component descriptor.
   *
   * @param componentContract
   *          the class of the component to create.
   * @return the query component instance.
   */
  IQueryComponent createQueryComponentInstance(
      Class<? extends IComponent> componentContract);

  /**
   * Gets the accessor factory used by this component factory.
   *
   * @return the accessor factory used by this component factory.
   */
  IAccessorFactory getAccessorFactory();

  /**
   * Sorts a component collection property.
   *
   * @param component
   *          the component to sort the collection property of.
   * @param propertyName
   *          the name of the collection property to sort.
   */
  void sortCollectionProperty(IComponent component, String propertyName);

  /**
   * Apply initialization mapping.
   *
   * @param component the component or query component to initialize
   * @param componentDescriptor the component descriptor
   * @param masterComponent the master component from which initialization is performed
   * @param initializationMapping the initialization mapping to apply
   */
  void applyInitializationMapping(Object component, IComponentDescriptor<?> componentDescriptor,
                                  Object masterComponent, Map<String, Object> initializationMapping);
}
