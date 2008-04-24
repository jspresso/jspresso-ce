/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.jspresso.framework.model.component.service.IComponentService;
import org.jspresso.framework.model.component.service.ILifecycleInterceptor;
import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.descriptor.IIconDescriptor;

/**
 * This interface is implemented by descriptors of components (java bean style).
 * Its is basically a composite of <code>IPropertyDescriptor</code>s.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @see org.jspresso.framework.model.descriptor.IPropertyDescriptor
 * @author Vincent Vandenschrick
 * @param <E>
 *            the concrete type of component.
 */
public interface IComponentDescriptor<E> extends IModelDescriptor,
    IIconDescriptor, IComponentDescriptorProvider<E>, ISecurable {

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
   * component.
   * 
   * @return the list of properties ordering the collections containing this
   *         component.
   */
  List<String> getOrderingProperties();

  /**
   * Retrieves the <code>IPropertyDescriptor</code> describing the property
   * whose name is passed in parameter.
   * 
   * @param propertyName
   *            the name of the property of which to look for the descriptor.
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
   * Get the default queryable properties of this component.
   * 
   * @return the default queryable properties of this component.
   */
  List<String> getQueryableProperties();

  /**
   * Get the default rendered properties of this component.
   * 
   * @return the default rendered properties of this component.
   */
  List<String> getRenderedProperties();

  /**
   * Retrieves list of service contracts implemented by this entity.
   * 
   * @return the class establishing the entity service contract.
   */
  Collection<Class<?>> getServiceContracts();

  /**
   * Retrieves the service delegate implemented by this component.
   * 
   * @param targetMethod
   *            the method invoked as service.
   * @return the <code>IComponentService</code> or null if the service does
   *         not exists.
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
   * Gets the set of properties which are erased during a clone operation of the
   * component.
   * 
   * @return the properties which must not be cloned.
   */
  Collection<String> getUnclonedProperties();

  /**
   * Gets wether this component descriptor is itself persistent or if it is only
   * an interface for other entities.
   * 
   * @return false if the interface defines a representation in the persistent
   *         storage.
   */
  boolean isComputed();

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

  /**
   * Gets the query contract of this component descriptor.
   * 
   * @return the query contract of this component descriptor.
   */
  Class<?> getQueryComponentContract();
}
