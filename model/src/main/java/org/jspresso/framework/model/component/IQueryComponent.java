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

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.util.collection.IPageable;
import org.jspresso.framework.util.collection.ISortable;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.lang.ICloneable;

/**
 * A simple adapter to wrap a component used as selection criteria and a list of
 * components. It only serve as a placeholder for the result of the query.
 * instances of this class do not perform queries by themselves.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision : 8051 $
 */
public interface IQueryComponent extends Map<String, Object>, IPageable, ISortable, ICloneable, IPropertyChangeCapable {

  /**
   * Ordering properties action constant.
   */
  String ORDERING_PROPERTIES = "ORDERING_PROPERTIES";

  /**
   * "queriedComponents" string constant.
   */
  String QUERIED_COMPONENTS = "queriedComponents";

  /**
   * Query component action constant.
   */
  String QUERY_COMPONENT = "QUERY_COMPONENT";

  /**
   * Null value.
   */
  String NULL_VAL = "#";

  /**
   * Not value.
   */
  String NOT_VAL = "!";

  /**
   * Disjunction.
   */
  String DISJUNCT = ";";

  /**
   * Gets the descriptor of this query component.
   *
   * @return the descriptor of this query component.
   */
  IComponentDescriptor<?> getComponentDescriptor();

  /**
   * Gets the descriptor of the queried components.
   *
   * @return the descriptor of the queried components.
   */
  IComponentDescriptor<?> getQueryDescriptor();

  /**
   * Gets the list of components result of the query.
   *
   * @param <T>
   *     type type of value.
   * @return the list of components result of the query.
   */
  <T> List<T> getQueriedComponents();

  /**
   * Gets the contract of the components to query.
   *
   * @param <T>
   *     type type of value.
   * @return the contract of the components to query.
   */
  <T> Class<T> getQueryContract();

  /**
   * Tests whether this query component maps an inline component or an entity.
   *
   * @return true if this query component map an inline component ?
   */
  boolean isInlineComponent();

  /**
   * Sets the default ordering properties that are used when not changed by the
   * user.
   *
   * @param defaultOrderingProperties
   *     the defaultOrderingProperties to set.
   */
  void setDefaultOrderingProperties(Map<String, ESort> defaultOrderingProperties);

  /**
   * Sets the list of components result of the query.
   *
   * @param queriedComponents
   *     the list of components result of the query.
   */
  void setQueriedComponents(List<?> queriedComponents);

  /**
   * Clones the query component.
   *
   * @return the query component clone.
   */
  @Override
  IQueryComponent clone();

  /**
   * Gets whether to enforce select distinct when querying.
   *
   * @return the distinctEnforced.
   */
  boolean isDistinctEnforced();

  /**
   * Performs any I18N dependent initialization on the query component.
   *
   * @param translationProvider
   *     the translation provider.
   * @param locale
   *     the session locale.
   */
  void translate(ITranslationProvider translationProvider, Locale locale);

  /**
   * Hydrates a query component with a hierarchical map holding bare filter
   * values.
   *
   * @param state
   *     the hierarchical map holding bare filter values.
   */
  void hydrate(Map<String, Object> state);

  /**
   * Pre-process a value before putting it into the query component. Transforms
   * a multi-line text into a disjunction of values.
   *
   * @param <T>
   *     type type of value.
   * @param value
   *     the value to refine to transform.
   * @param propertyDescriptor
   *     the property descriptor this value should be refined for.
   * @return the refined value.
   */
  <T> T refineValue(T value, IPropertyDescriptor propertyDescriptor);

  /**
   * Is the query component restricting.
   *
   * @return the boolean
   */
  boolean isRestricting();

  /**
   * Gets prefetch properties.
   *
   * @return the prefetch properties
   */
  List<String> getPrefetchProperties();

  /**
   * Sets prefetch properties.
   *
   * @param prefetchProperties
   *     the prefetch properties
   */
  void setPrefetchProperties(List<String> prefetchProperties);

  /**
   * Resets the query component.
   */
  void reset();

  /**
   * Set the extra component.
   * @param extra The component.
   */
  void setExtra(IComponent extra);

  /**
   * Get the extra component.
   * @return the extra component.
   */
  IComponent getExtra() ;

}
