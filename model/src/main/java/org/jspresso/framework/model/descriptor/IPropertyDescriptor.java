/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

import java.util.List;

import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.bean.integrity.IPropertyProcessor;
import org.jspresso.framework.util.gate.IGateAccessible;

/**
 * This interface is the super-interface of all properties descriptors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 * @see org.jspresso.framework.model.descriptor.IComponentDescriptor
 */
public interface IPropertyDescriptor extends IModelDescriptor, Cloneable,
    ISecurable, IGateAccessible {

  /**
   * Clones this descriptor.
   * 
   * @return the descriptor's clone.
   */
  IPropertyDescriptor clone();

  /**
   * Creates a new property descriptor to allow for querying.
   * 
   * @return a new property descriptor that allows for expressing constraints on
   *         this property.
   */
  IPropertyDescriptor createQueryDescriptor();

  /**
   * Gets the <code>Class</code> of the delegates used to compute the values of
   * the property or <code>null</code> if this property is not a derived one.
   * 
   * @return The class of the extension delegates used to compute the property.
   */
  Class<?> getDelegateClass();

  /**
   * Gets the <code>Class</code> name of the delegates used to compute the
   * values of the property or <code>null</code> if this property is not a
   * derived one.
   * 
   * @return The class of the extension delegates used to compute the property.
   */
  String getDelegateClassName();

  /**
   * Gets the collection of <code>IIntegrityProcessor</code> s which are
   * registered as pre-processors and post-processors.
   * 
   * @return the registered <code>IIntegrityProcessor</code> s
   */
  List<IPropertyProcessor<?, ?>> getIntegrityProcessors();

  /**
   * Gets the scope on which the property is unique.
   * 
   * @return the unicity scope.
   */
  String getUnicityScope();

  /**
   * Triggers all setter interceptors.
   * 
   * @param component
   *          the component targetted by the setter.
   * @param newValue
   *          the property new value.
   * @return the result of the interception.
   */
  Object interceptSetter(Object component, Object newValue);

  /**
   * Wether the underlying property is mandatory.
   * 
   * @return true if mandatory
   */
  boolean isMandatory();

  /**
   * Wether the underlying property has a modifier. This is only usefull
   * whenever the property is computed by delegation. In this case the delegate
   * should be analyzed to check whether it has a modifier on the property.
   * 
   * @return true if the property has a modifier.
   */
  boolean isModifiable();

  /**
   * Gets wether this descriptor is an overload of a parent one.
   * 
   * @return true if this descriptor is an overload of a parent one.
   */
  boolean isOverload();

  /**
   * Gets wether this kind of property descriptor is queryable.
   * 
   * @return true if this kind of property descriptor is queryable.
   */
  boolean isQueryable();

  /**
   * Triggers all setter postprocessors.
   * 
   * @param component
   *          the component targetted by the setter.
   * @param oldValue
   *          the property old value.
   * @param newValue
   *          the property new value.
   */
  void postprocessSetter(Object component, Object oldValue, Object newValue);

  /**
   * Triggers all setter preprocessors.
   * 
   * @param component
   *          the component targetted by the setter.
   * @param newValue
   *          the property new value.
   */
  void preprocessSetter(Object component, Object newValue);

  /**
   * Gets wether this property is computed (derived). Most of the time a
   * property is computed whenever it has a delegate to compute its value by
   * opposition to properties handled by the ORM.
   * 
   * @return true if the property is computed.
   */
  boolean isComputed();
}
