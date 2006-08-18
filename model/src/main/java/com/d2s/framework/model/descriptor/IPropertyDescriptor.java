/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

import java.util.Collection;
import java.util.List;

import com.d2s.framework.model.integrity.IPropertyIntegrityProcessor;
import com.d2s.framework.util.IGate;

/**
 * This interface is the super-interface of all properties descriptors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @see com.d2s.framework.model.descriptor.IComponentDescriptor
 */
public interface IPropertyDescriptor extends IModelDescriptor {

  /**
   * Wether the underlying property is mandatory.
   * 
   * @return true if mandatory
   */
  boolean isMandatory();

  /**
   * Gets the collection of <code>IIntegrityProcessor</code> s which are
   * registered as pre-processors and post-processors.
   * 
   * @return the registered <code>IIntegrityProcessor</code> s
   */
  List<IPropertyIntegrityProcessor> getIntegrityProcessors();

  /**
   * Gets the <code>Class</code> of the delegates used to compute the values
   * of the property or <code>null</code> if this property is not a derived
   * one.
   * 
   * @return The class of the extension delegates used to compute the property.
   */
  Class getDelegateClass();

  /**
   * Gets the <code>Class</code> name of the delegates used to compute the
   * values of the property or <code>null</code> if this property is not a
   * derived one.
   * 
   * @return The class of the extension delegates used to compute the property.
   */
  String getDelegateClassName();

  /**
   * Gets the scope on which the property is unique.
   * 
   * @return the unicity scope.
   */
  String getUnicityScope();

  /**
   * Wether the underlying property is computed.
   * 
   * @return true if computed
   */
  boolean isComputed();

  /**
   * Gets wether this kind of property descriptor is queryable.
   * 
   * @return true if this kind of property descriptor is queryable.
   */
  boolean isQueryable();

  /**
   * Gets wether this descriptor is an overload of a parent one.
   * 
   * @return true if this descriptor is an overload of a parent one.
   */
  boolean isOverload();

  /**
   * Wether the underlying property is read-only.
   * 
   * @return true if read-only
   */
  boolean isReadOnly();

  /**
   * Gets the collection of gates determining the readability state of this
   * property.
   * 
   * @return the collection of gates determining the readability state of this
   *         property.
   */
  Collection<IGate> getReadabilityGates();

  /**
   * Gets the collection of gates determining the writability state of this
   * property.
   * 
   * @return the collection of gates determining the writability state of this
   *         property.
   */
  Collection<IGate> getWritabilityGates();
}
