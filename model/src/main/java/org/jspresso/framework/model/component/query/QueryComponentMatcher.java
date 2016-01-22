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
package org.jspresso.framework.model.component.query;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.exception.NestedRuntimeException;


/**
 * This is a utility service that knows how to match an arbitrary bean or map against a query component.
 *
 * @author Vincent Vandenschrick
 */
public class QueryComponentMatcher {

  private Map<String, Object>                filteringConditions;
  private Map<String, QueryComponentMatcher> subMatchers;
  private IAccessorFactory                   accessorFactory;
  private boolean                            caseSensitive;
  private boolean                            matchesMiddle;

  /**
   * Instantiates a new Query component matcher.
   *
   * @param queryComponent
   *     the query component
   * @param accessorFactory
   *     the accessor factory
   * @param caseSensitive
   *     the case sensitive
   * @param matchesMiddle
   *     the matches middle
   */
  public QueryComponentMatcher(IQueryComponent queryComponent, IAccessorFactory accessorFactory, boolean caseSensitive,
                               boolean matchesMiddle) {
    this.subMatchers = new HashMap<>();
    this.accessorFactory = accessorFactory;
    this.caseSensitive = caseSensitive;
    this.matchesMiddle = matchesMiddle;
    this.filteringConditions = computeFilteringConditions(queryComponent);
  }

  /**
   * Filter collection.
   *
   * @param collection
   *     the collection
   * @return the list
   */
  public List<?> filterCollection(List<?> collection) {
    List<Object> filtered = new ArrayList<>();
    if (filteringConditions != null && !filteringConditions.isEmpty()) {
      for (Object element : collection) {
        if (componentMatches(element)) {
          filtered.add(element);
        }
      }
    } else {
      filtered.addAll(collection);
    }
    return filtered;
  }

  /**
   * Component matches.
   *
   * @param component
   *     the component
   * @return the boolean
   */
  public boolean componentMatches(Object component) {
    for (Map.Entry<String, Object> condition : filteringConditions.entrySet()) {
      if (!conditionMatches(component, condition.getKey(), condition.getValue())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Condition matches.
   *
   * @param component
   *     the component
   * @param propertyName
   *     the property name
   * @param predicate
   *     the predicate
   * @return the boolean
   */
  protected boolean conditionMatches(Object component, String propertyName, Object predicate) {
    if (predicate instanceof IQueryComponent && ((IQueryComponent) predicate).isEmpty()) {
      return true;
    }
    String[] nestedProps = propertyName.split("\\.");
    Object property = component;
    for (String nestedProperty : nestedProps) {
      if (property != null) {
        try {
          property = getAccessorFactory().createPropertyAccessor(nestedProperty, property.getClass()).getValue(
              property);
        } catch (IllegalAccessException | NoSuchMethodException e) {
          throw new NestedRuntimeException(e);
        } catch (InvocationTargetException e) {
          throw new NestedRuntimeException(e.getTargetException());
        }
      }
    }

    if (property instanceof Collection<?>) {
      for (Object propertyElement : (Collection<?>) property) {
        if (propertyMatches(component, propertyName, propertyElement, predicate)) {
          return true;
        }
      }
    } else {
      return propertyMatches(component, propertyName, property, predicate);
    }
    return false;
  }

  /**
   * Property matches.
   *
   * @param component
   *     the component
   * @param propertyName
   *     the property name
   * @param property
   *     the property
   * @param predicate
   *     the predicate
   * @return the boolean
   */
  @SuppressWarnings({"unused", "unchecked"})
  protected boolean propertyMatches(Object component, String propertyName, Object property, Object predicate) {
    if (predicate instanceof ComparableQueryStructure) {
      return ((ComparableQueryStructure) predicate).matches((Comparable<Object>) property);
    } else if (predicate instanceof IQueryComponent) {
      return getSubMatcher(propertyName, (IQueryComponent) predicate).componentMatches(property);
    } else if (predicate instanceof String) {
      String regex = (String) predicate;
      String[] disjunctions = regex.split(QueryComponent.DISJUNCT);
      if (disjunctions.length > 1) {
        for (String regexPart : disjunctions) {
          if (propertyMatches((String) property, regexPart)) {
            return true;
          }
        }
      } else {
        return propertyMatches((String) property, regex);
      }
    }
    return true;
  }

  /**
   * Property matches.
   *
   * @param property
   *     the property
   * @param regex
   *     the regex
   * @return the boolean
   */
  protected boolean propertyMatches(String property, String regex) {
    boolean negate = false;
    if (regex.startsWith(QueryComponent.NOT_VAL)) {
      negate = true;
      regex = regex.substring(1);
    }
    boolean matches;
    if (regex.startsWith(QueryComponent.NULL_VAL)) {
      matches = (property == null || property.length() == 0);
    } else {
      if (property == null) {
        matches = false;
      } else {
        if (caseSensitive) {
          matches = Pattern.compile(regex).matcher(property).matches();
        } else {
          matches = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(property).matches();
        }
      }
    }
    if (negate) {
      matches = !matches;
    }
    return matches;
  }

  private Map<String, Object> computeFilteringConditions(IQueryComponent queryComponent) {
    Map<String, Object> conditions = new HashMap<>();
    for (Map.Entry<String, Object> qcProp : queryComponent.entrySet()) {
      String k = qcProp.getKey();
      Object v = qcProp.getValue();
      IPropertyDescriptor pd = queryComponent.getQueryDescriptor().getPropertyDescriptor(k);
      if (pd != null && v != null && !(v instanceof ComparableQueryStructure && !((ComparableQueryStructure) v)
          .isRestricting())) {
        if (v instanceof String) {
          String regex = (String) v;
          regex = regex.replaceAll("%", ".*");
          regex = regex.replaceAll("_", ".");
          regex += ".*";
          if (matchesMiddle) {
            regex = ".*" + regex;
          }
          conditions.put(k, regex);
        } else {
          conditions.put(k, v);
        }
      }
    }
    return conditions;
  }

  private QueryComponentMatcher getSubMatcher(String propertyName, IQueryComponent subQueryComponent) {
    QueryComponentMatcher subMatcher = subMatchers.get(propertyName);
    if (subMatcher == null) {
      subMatcher = new QueryComponentMatcher(subQueryComponent, accessorFactory, caseSensitive, matchesMiddle);
      subMatchers.put(propertyName, subMatcher);
    }
    return subMatcher;
  }

  /**
   * Gets accessor factory.
   *
   * @return the accessor factory
   */
  protected IAccessorFactory getAccessorFactory() {
    return accessorFactory;
  }
}
