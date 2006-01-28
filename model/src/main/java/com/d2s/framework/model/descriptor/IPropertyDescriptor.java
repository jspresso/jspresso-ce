/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

import java.util.List;

import com.d2s.framework.model.integrity.IPropertyIntegrityProcessor;

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
   * Gets the class of this property.
   * 
   * @return the property class.
   */
  Class getPropertyClass();

  /**
   * Gets the scope on which the property is unique.
   * 
   * @return the unicity scope.
   */
  String getUnicityScope();
}
