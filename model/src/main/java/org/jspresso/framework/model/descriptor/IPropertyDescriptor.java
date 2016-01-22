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
package org.jspresso.framework.model.descriptor;

import java.util.List;

import org.jspresso.framework.security.ISecurable;
import org.jspresso.framework.util.automation.IPermIdSource;
import org.jspresso.framework.util.bean.integrity.IPropertyProcessor;
import org.jspresso.framework.util.gate.IGateAccessible;
import org.jspresso.framework.util.lang.ICloneable;

/**
 * This interface is the super-interface of all properties descriptors.
 *
 * @author Vincent Vandenschrick
 * @see org.jspresso.framework.model.descriptor.IComponentDescriptor
 */
public interface IPropertyDescriptor extends IModelDescriptor, ICloneable, ISecurable, IGateAccessible, IPermIdSource {

  /**
   * Clones this descriptor.
   *
   * @return the descriptor clone.
   */
  @Override
  IPropertyDescriptor clone();

  /**
   * Creates a new property descriptor to allow for querying.
   *
   * @return a new property descriptor that allows for expressing constraints on
   * this property.
   */
  IPropertyDescriptor createQueryDescriptor();

  /**
   * Gets the {@code Class} of the delegates used to compute the values of
   * the property or {@code null} if this property is not a derived one.
   *
   * @return The class of the extension delegates used to compute the property.
   */
  Class<?> getDelegateClass();

  /**
   * Gets the {@code Class} name of the delegates used to compute the
   * values of the property or {@code null} if this property is not a
   * derived one.
   *
   * @return The class of the extension delegates used to compute the property.
   */
  String getDelegateClassName();

  /**
   * Gets the collection of {@code IIntegrityProcessor} s which are
   * registered as pre-processors and post-processors.
   *
   * @return the registered {@code IIntegrityProcessor} s
   */
  List<IPropertyProcessor<?, ?>> getIntegrityProcessors();

  /**
   * Gives a preferred width hint for representing the property.
   *
   * @return a preferred width hint for representing the property.
   */
  Integer getPreferredWidth();

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
   *     the component targeted by the setter.
   * @param newValue
   *     the property new value.
   * @return the result of the interception.
   */
  Object interceptSetter(Object component, Object newValue);

  /**
   * Gets whether this property is computed (derived). Most of the time a
   * property is computed whenever it has a delegate to compute its value by
   * opposition to properties handled by the ORM.
   *
   * @return true if the property is computed.
   */
  boolean isComputed();

  /**
   * Whether the underlying property is mandatory.
   *
   * @return true if mandatory
   */
  boolean isMandatory();

  /**
   * Whether the underlying property has a modifier. This is only useful
   * whenever the property is computed by delegation. In this case the delegate
   * should be analyzed to check whether it has a modifier on the property.
   *
   * @return true if the property has a modifier.
   */
  boolean isModifiable();

  /**
   * Gets whether this kind of property descriptor is queryable.
   *
   * @return true if this kind of property descriptor is queryable.
   */
  boolean isQueryable();

  /**
   * Triggers all setter post processors.
   *
   * @param component
   *     the component targeted by the setter.
   * @param oldValue
   *     the property old value.
   * @param newValue
   *     the property new value.
   */
  void postprocessSetter(Object component, Object oldValue, Object newValue);

  /**
   * Triggers all setter preprocessors.
   *
   * @param component
   *     the component targeted by the setter.
   * @param newValue
   *     the property new value.
   */
  void preprocessSetter(final Object component, final Object newValue);

  /**
   * Gets the formula used to represent this property in the persistent store.
   * This can be used to order / group by computed properties.
   *
   * @return the persistence formula.
   */
  String getPersistenceFormula();

  /**
   * Gets whether this property can be used for sorting, in a tabular view for
   * instance.
   *
   * @return {@code true} if this property can be used for sorting.
   */
  boolean isSortable();

  /**
   * Gets whether this property can be cached. This is only used for computed
   * properties. Note that the cached value will be reset whenever a
   * firePropertyChange regarding this property is detected to be fired.
   *
   * @return {@code true} if this property is cacheable.
   */
  boolean isCacheable();

  /**
   * Gets whether this property is filter comparable, i.e. a comparator is installed in any filter view based on this
   * property.
   * By default naturally comparable property types are filter comparable. But there are case when you might want to
   * override this behaviour in a sense or another. see {@link <a href="https://github.com/jspresso/jspresso-ce/issues/12"> this bug report </a>}
   *
   * @return {@code true} if this property is comparable.
   */
  boolean isFilterComparable();
}
