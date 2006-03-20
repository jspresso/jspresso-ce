/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import com.d2s.framework.model.service.IComponentService;
import com.d2s.framework.util.descriptor.IIconDescriptor;

/**
 * This interface is implemented by descriptors of components (java bean style).
 * Its is basically a composite of <code>IPropertyDescriptor</code>s.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @see com.d2s.framework.model.descriptor.IPropertyDescriptor
 * @author Vincent Vandenschrick
 */
public interface IComponentDescriptor extends IModelDescriptor,
    IIconDescriptor, IComponentDescriptorProvider {

  /**
   * Gets the collection of the properties descriptors of this component
   * descriptor.
   * 
   * @return the collection of <code>IPropertyDescriptor</code>s.
   */
  Collection<IPropertyDescriptor> getPropertyDescriptors();

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
   * Gets the interface class defining the component contract.
   * 
   * @return the interface class defining the component contract.
   */
  Class<?> getComponentContract();

  /**
   * Retrieves the service delegate implemented by this component.
   * 
   * @param targetMethod
   *          the method invoked as service.
   * @return the <code>IComponentService</code> or null if the service does
   *         not exists.
   */
  IComponentService getServiceDelegate(Method targetMethod);

  /**
   * Retrieves list of service contracts implemented by this entity.
   * 
   * @return the class establishing the entity service contract.
   */
  Collection<Class> getServiceContracts();

  /**
   * Gets the set of properties which are erased during a clone operation of the
   * component.
   * 
   * @return the properties which must not be cloned.
   */
  Collection<String> getUnclonedProperties();

  /**
   * Gets the descriptor ancestors collection. It directly translates the
   * components inheritance hierarchy since the component property descriptors
   * are the union of the declared property descriptors of the component and of
   * its ancestors one. A component may have multiple ancestors which means that
   * complex multi-inheritance hierarchy can be mapped.
   * 
   * @return ancestorDescriptors The list of ancestor entity descriptors.
   */
  List<IComponentDescriptor> getAncestorDescriptors();

  /**
   * Gets the collection of the properties descriptors this entity descriptor
   * declares (excluding the ones of its ancestors).
   * 
   * @return the collection of <code>IPropertyDescriptor</code>s.
   */
  Collection<IPropertyDescriptor> getDeclaredPropertyDescriptors();

  /**
   * Get the list of properties ordering the collections containing this
   * component.
   * 
   * @return the list of properties ordering the collections containing this
   *         component.
   */
  List<String> getOrderingProperties();

  /**
   * Get the default rendered properties of this component.
   * 
   * @return the default rendered properties of this component.
   */
  List<String> getRenderedProperties();

  /**
   * Get the default queryable properties of this component.
   * 
   * @return the default queryable properties of this component.
   */
  List<String> getQueryableProperties();

  /**
   * Gets the property used to build the toString() representation of the
   * component.
   * 
   * @return the property used to build the toString() representation of the
   *         component.
   */
  String getToStringProperty();

  /**
   * Gets wether this component descriptor is itself persistent or if it is only
   * an interface for other entities.
   * 
   * @return false if the interface defines a representation in the persistent
   *         storage.
   */
  boolean isComputed();

  /**
   * Gets wether this entity descriptor is a pure abstract definition. Only
   * descendents of this descriptor can be instanciated.
   * 
   * @return true if this is a pure abstract entity descriptor.
   */
  boolean isPurelyAbstract();
}
