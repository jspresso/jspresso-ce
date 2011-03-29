/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.descriptor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.component.service.IComponentService;
import org.jspresso.framework.model.component.service.ILifecycleInterceptor;
import org.jspresso.framework.util.automation.IPermIdSource;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.util.descriptor.IIconDescriptor;
import org.jspresso.framework.util.gate.IGateAccessible;

/**
 * This interface is implemented by descriptors of components (java bean style).
 * Its is basically a composite of <code>IPropertyDescriptor</code>s.
 * 
 * @version $LastChangedRevision$
 * @see org.jspresso.framework.model.descriptor.IPropertyDescriptor
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete type of component.
 */
public interface IComponentDescriptor<E> extends IIconDescriptor,
    IComponentDescriptorProvider<E>, IGateAccessible, IPermIdSource {

  /**
   * Creates a new component descriptor to allow for querying.
   * 
   * @return a new component descriptor that allows for expressing constraints
   *         on this component.
   */
  IComponentDescriptor<E> createQueryDescriptor();

  /**
   * Gets the interface class defining the component contract.
   * 
   * @return the interface class defining the component contract.
   */
  Class<? extends E> getComponentContract();

  /**
   * Gets the collection of the properties descriptors this entity descriptor
   * declares (excluding the ones of its ancestors).
   * 
   * @return the collection of <code>IPropertyDescriptor</code>s.
   */
  Collection<IPropertyDescriptor> getDeclaredPropertyDescriptors();

  /**
   * Gets the entity lifecycle interceptors.
   * 
   * @return the list of entity lifecycle interceptors.
   */
  List<ILifecycleInterceptor<?>> getLifecycleInterceptors();

  /**
   * Get the list of properties ordering the collections containing this
   * component along with their individual sorting direction.
   * 
   * @return the map of properties ordering the collections containing this
   *         component.
   */
  Map<String, ESort> getOrderingProperties();

  /**
   * Whenever this component is used as a query filter, this is the default page
   * size applied for the query.
   * 
   * @return the page size for the query when used as query filter.
   */
  Integer getPageSize();

  /**
   * Retrieves the <code>IPropertyDescriptor</code> describing the property
   * whose name is passed in parameter.
   * 
   * @param propertyName
   *          the name of the property of which to look for the descriptor.
   * @return the <code>IPropertyDescriptor</code> or null if the property does
   *         not exists.
   */
  IPropertyDescriptor getPropertyDescriptor(String propertyName);

  /**
   * Gets the collection of the properties descriptors of this component
   * descriptor.
   * 
   * @return the collection of <code>IPropertyDescriptor</code>s.
   */
  Collection<IPropertyDescriptor> getPropertyDescriptors();

  /**
   * Gets the query contract of this component descriptor.
   * 
   * @return the query contract of this component descriptor.
   */
  Class<?> getQueryComponentContract();

  /**
   * Retrieves list of service contract class names implemented by this
   * component.
   * 
   * @return the list of service contract class names implemented by this
   *         component.
   */
  Collection<String> getServiceContractClassNames();

  /**
   * Retrieves list of service contracts implemented by this component.
   * 
   * @return the list of service contracts implemented by this component.
   */
  Collection<Class<?>> getServiceContracts();

  /**
   * Retrieves the service delegate implemented by this component.
   * 
   * @param targetMethod
   *          the method invoked as service.
   * @return the <code>IComponentService</code> or null if the service does not
   *         exists.
   */
  IComponentService getServiceDelegate(Method targetMethod);

  /**
   * Gets the property used to build the toString() representation of the
   * component.
   * 
   * @return the property used to build the toString() representation of the
   *         component.
   */
  String getToStringProperty();

  /**
   * Gets the property used to autocomplete when performing a LOV.
   * 
   * @return the property used to autocomplete when performing a LOV.
   */
  String getAutoCompleteProperty();

  /**
   * Gets the set of properties which are erased during a clone operation of the
   * component.
   * 
   * @return the properties which must not be cloned.
   */
  Collection<String> getUnclonedProperties();

  /**
   * Gets wether the component described is an entity.
   * 
   * @return true if the component described is an entity.
   */
  boolean isEntity();

  /**
   * Gets wether this entity descriptor is a pure abstract definition. Only
   * descendents of this descriptor can be instanciated.
   * 
   * @return true if this is a pure abstract entity descriptor.
   */
  boolean isPurelyAbstract();
}
