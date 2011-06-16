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
package org.jspresso.framework.model.component;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.util.collection.IPageable;
import org.jspresso.framework.util.collection.ISortable;

/**
 * A simple adapter to wrap a component used as selection criteria and a list of
 * components. It only serve as a placeholder for the result of the query.
 * instances of this calss do not perform queries by themselves.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IQueryComponent extends Map<String, Object>, IPageable,
    ISortable, Cloneable {

  /**
   * Ordering properties action constant.
   */
  String ORDERING_PROPERTIES = "ORDERING_PROPERTIES";

  /**
   * "queriedComponents" string constant.
   */
  String QUERIED_COMPONENTS  = "queriedComponents";

  /**
   * Query component action constant.
   */
  String QUERY_COMPONENT     = "QUERY_COMPONENT";

  /**
   * Null value.
   */
  String NULL_VAL            = "#";

  /**
   * Not value.
   */
  String NOT_VAL             = "!";

  /**
   * Gets the descriptor of the queried components.
   * 
   * @return the descriptor of the queried components.
   */
  IComponentDescriptor<?> getComponentDescriptor();

  /**
   * Gets the list of components result of the query.
   * 
   * @return the list of components result of the query.
   */
  List<?> getQueriedComponents();

  /**
   * Gets the contract of the components to query.
   * 
   * @return the contract of the components to query.
   */
  Class<?> getQueryContract();

  /**
   * Does this query component map an inline component or an entity ?
   * 
   * @return true if this query component map an inline component ?
   */
  boolean isInlineComponent();

  /**
   * Sets the default ordering properties that are used when not changed by the
   * user.
   * 
   * @param defaultOrderingProperties
   *          the defaultOrderingProperties to set.
   */
  void setDefaultOrderingProperties(Map<String, ESort> defaultOrderingProperties);

  /**
   * Sets the list of components result of the query.
   * 
   * @param queriedComponents
   *          the list of components result of the query.
   */
  void setQueriedComponents(List<?> queriedComponents);

  /**
   * Clones the query component.
   * 
   * @return the query component clone.
   */
  IQueryComponent clone();
}
