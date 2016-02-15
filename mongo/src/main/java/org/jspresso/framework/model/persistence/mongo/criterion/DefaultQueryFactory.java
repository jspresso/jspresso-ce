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
package org.jspresso.framework.model.persistence.mongo.criterion;

import static org.springframework.data.mongodb.core.query.Criteria.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.jspresso.framework.application.action.AbstractActionContextAware;
import org.jspresso.framework.application.backend.action.persistence.mongo.QueryEntitiesAction;
import org.jspresso.framework.model.component.IPropertyTranslation;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.ComparableQueryStructure;
import org.jspresso.framework.model.component.query.EnumQueryStructure;
import org.jspresso.framework.model.component.query.EnumValueQueryStructure;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.AbstractComponentDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;
import org.jspresso.framework.model.entity.EntityHelper;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.bean.PropertyHelper;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.view.descriptor.basic.PropertyViewDescriptorHelper;

/**
 * Default implementation of a query factory.
 *
 * @author Vincent Vandenschrick
 */
public class DefaultQueryFactory extends AbstractActionContextAware implements IQueryFactory {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultQueryFactory.class);

  private static final String SPECIAL_CHARS = "${}*^";

  private boolean triStateBooleanSupported;

  /**
   * Constructs a new {@code DefaultQueryFactory} instance.
   */
  public DefaultQueryFactory() {
    triStateBooleanSupported = false;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("ConstantConditions")
  @Override
  public void completeQueryWithOrdering(Query query, IQueryComponent queryComponent, Map<String, Object> context) {
    // complete sorting properties
    if (queryComponent.getOrderingProperties() != null) {
      List<Sort.Order> sortOrders = new ArrayList<>();
      for (Map.Entry<String, ESort> orderingProperty : queryComponent.getOrderingProperties().entrySet()) {
        String propertyName = orderingProperty.getKey();
        String[] propElts = propertyName.split("\\.");
        boolean sortable = true;
        if (propElts.length > 1) {
          IComponentDescriptor<?> currentCompDesc = queryComponent.getQueryDescriptor();
          int i = 0;
          for (; sortable && i < propElts.length - 1; i++) {
            IReferencePropertyDescriptor<?> refPropDescriptor = ((IReferencePropertyDescriptor<?>) currentCompDesc
                .getPropertyDescriptor(propElts[i]));
            if (refPropDescriptor != null) {
              sortable = sortable && isSortable(refPropDescriptor);
              if (EntityHelper.isInlineComponentReference(refPropDescriptor)) {
                break;
              }
              currentCompDesc = refPropDescriptor.getReferencedDescriptor();
            } else {
              LOG.error("Ordering property {} not found on {}", propElts[i],
                  currentCompDesc.getComponentContract().getName());
              sortable = false;
            }
          }
          if (sortable) {
            StringBuilder name = new StringBuilder();
            for (int j = i; sortable && j < propElts.length; j++) {
              IPropertyDescriptor propDescriptor = currentCompDesc.getPropertyDescriptor(propElts[j]);
              sortable = sortable && isSortable(propDescriptor);
              if (j < propElts.length - 1) {
                currentCompDesc = ((IReferencePropertyDescriptor<?>) propDescriptor).getReferencedDescriptor();
              }
              if (j > i) {
                name.append(".");
              }
              name.append(propElts[j]);
            }
            if (sortable) {
              propertyName = name.toString();
            }
          }
        } else {
          IPropertyDescriptor propertyDescriptor = queryComponent.getQueryDescriptor().getPropertyDescriptor(
              propertyName);
          if (propertyDescriptor != null) {
            sortable = isSortable(propertyDescriptor);
          } else {
            LOG.error("Ordering property {} not found on {}", propertyName,
                queryComponent.getQueryDescriptor().getComponentContract().getName());
            sortable = false;
          }
        }
        if (sortable) {
          Sort.Order order;
          switch (orderingProperty.getValue()) {
            case DESCENDING:
              order = new Sort.Order(Sort.Direction.DESC, PropertyHelper.toJavaBeanPropertyName(propertyName));
              break;
            case ASCENDING:
            default:
              order = new Sort.Order(Sort.Direction.ASC, PropertyHelper.toJavaBeanPropertyName(propertyName));
          }
          sortOrders.add(order);
        }
      }
      query.with(new Sort(sortOrders));
    }
  }

  private boolean isSortable(IPropertyDescriptor propertyDescriptor) {
    return propertyDescriptor != null && (!propertyDescriptor.isComputed()
        || propertyDescriptor.getPersistenceFormula() != null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Query createQuery(IQueryComponent queryComponent, Map<String, Object> context) {
    Query query = new Query();
    boolean abort = completeQuery(query, null, queryComponent, context);
    if (abort) {
      return null;
    }
    return query;
  }

  @SuppressWarnings("ConstantConditions")
  private boolean completeQuery(Query query, String path, IQueryComponent aQueryComponent,
                                Map<String, Object> context) {
    boolean abort = false;
    IComponentDescriptor<?> componentDescriptor = aQueryComponent.getQueryDescriptor();
    if (aQueryComponent instanceof ComparableQueryStructure) {
      completeQuery(query, createComparableQueryStructureRestriction(path, (ComparableQueryStructure) aQueryComponent,
          componentDescriptor, aQueryComponent, context));
    } else {
      String translationsPath = AbstractComponentDescriptor.getComponentTranslationsDescriptorTemplate().getName();
      for (Map.Entry<String, Object> property : aQueryComponent.entrySet()) {
        String propertyName = property.getKey();
        Object propertyValue = property.getValue();
        IPropertyDescriptor propertyDescriptor = componentDescriptor.getPropertyDescriptor(propertyName);
        if (propertyDescriptor != null) {
          boolean isEntityRef = false;
          if (componentDescriptor.isEntity() && aQueryComponent.containsKey(IEntity.ID)) {
            isEntityRef = true;
          }
          if ((!PropertyViewDescriptorHelper.isComputed(componentDescriptor, propertyName) || (
              propertyDescriptor instanceof IStringPropertyDescriptor
                  && ((IStringPropertyDescriptor) propertyDescriptor).isTranslatable())) && (!isEntityRef || IEntity.ID
              .equals(propertyName))) {
            String prefixedProperty;
            if (path != null) {
              prefixedProperty = path + "." + propertyName;
            } else {
              prefixedProperty = propertyName;
            }
            if (propertyValue instanceof IEntity) {
              if (!((IEntity) propertyValue).isPersistent()) {
                abort = true;
              } else {
                completeQuery(query, where(prefixedProperty).is(propertyValue));
              }
            } else if (propertyValue instanceof Boolean && (isTriStateBooleanSupported() || (Boolean) propertyValue)) {
              completeQuery(query, where(prefixedProperty).is(propertyValue));
            } else if (IEntity.ID.equalsIgnoreCase(propertyName)) {
              completeQuery(query,
                  createIdRestriction(propertyDescriptor, prefixedProperty, propertyValue, componentDescriptor,
                      aQueryComponent, context));
            } else if (propertyValue instanceof String) {
              completeQueryWithTranslations(query, translationsPath, translationsPath, property, propertyDescriptor,
                  prefixedProperty, getBackendController(context).getLocale(), componentDescriptor, aQueryComponent,
                  context);
            } else if (propertyValue instanceof Number || propertyValue instanceof Date) {
              completeQuery(query, where(prefixedProperty).is(propertyValue));
            } else if (propertyValue instanceof EnumQueryStructure) {
              completeQuery(query, createEnumQueryStructureRestriction(prefixedProperty, ((EnumQueryStructure) propertyValue)));
            } else if (propertyValue instanceof IQueryComponent) {
              IQueryComponent joinedComponent = ((IQueryComponent) propertyValue);
              if (!isQueryComponentEmpty(joinedComponent, propertyDescriptor)) {
                if (joinedComponent.isInlineComponent()/* || path != null */) {
                  // the joined component is an inline component so we must use
                  // dot nested properties. Same applies if we are in a nested
                  // path i.e. already on an inline component.
                  abort = abort || completeQuery(query, prefixedProperty, (IQueryComponent) propertyValue, context);
                } else {
                  // the joined component is an entity so we must use
                  // nested query; unless the autoComplete property
                  // is a special char.
                  boolean digDeeper = true;
                  String autoCompleteProperty = joinedComponent.getQueryDescriptor().getAutoCompleteProperty();
                  if (autoCompleteProperty != null) {
                    String val = (String) joinedComponent.get(autoCompleteProperty);
                    if (val != null) {
                      boolean negate = false;
                      if (val.startsWith(IQueryComponent.NOT_VAL)) {
                        val = val.substring(1);
                        negate = true;
                      }
                      if (IQueryComponent.NULL_VAL.equals(val)) {
                        Criteria crit;
                        if (negate) {
                          crit = where(prefixedProperty).ne(null);
                        } else {
                          crit = where(prefixedProperty).is(null);
                        }
                        completeQuery(query, crit);
                        digDeeper = false;
                      }
                    }
                  }
                  if (digDeeper) {
                    abort = abort || completeQuery(query, prefixedProperty, joinedComponent, context);
                  }
                }
              }
            } else if (propertyValue != null) {
              // Unknown property type. Assume equals.
              completeQuery(query, where(prefixedProperty).is(propertyValue));
            }
          }
        }
      }
    }
    return abort;
  }

  /**
   * Complete with criteria.
   *
   * @param currentQuery
   *     the current query
   * @param criteria
   *     the criteria
   */
  protected void completeQuery(Query currentQuery, Criteria criteria) {
    if (criteria != null) {
      currentQuery.addCriteria(criteria);
    }
  }

  /**
   * Complements a query by processing an enumeration query structure.
   *
   * @param path
   *     the path to the comparable property.
   * @param enumQueryStructure
   *     the collection of checked / unchecked enumeration values.
   * @return the created criteria or null if no criteria necessary.
   */
  protected Criteria createEnumQueryStructureRestriction(String path, EnumQueryStructure enumQueryStructure) {
    Set<String> inListValues = new HashSet<>();
    boolean nullAllowed = false;
    for (EnumValueQueryStructure inListValue : enumQueryStructure.getSelectedEnumerationValues()) {
      if (inListValue.getValue() == null || "".equals(inListValue.getValue())) {
        nullAllowed = true;
      } else {
        inListValues.add(inListValue.getValue());
      }
    }
    if (!inListValues.isEmpty()) {
      List<Criteria> disjunctions = new ArrayList<>();
      disjunctions.add(where(path).in(inListValues));
      if (nullAllowed) {
        disjunctions.add(where(path).is(null));
      }
      if (disjunctions.size() == 1) {
        return disjunctions.get(0);
      }
      return where(path).orOperator(disjunctions.toArray(new Criteria[disjunctions.size()]));
    }
    return null;
  }

  /**
   * Creates an id based restriction.
   *
   * @param propertyDescriptor
   *     the id property descriptor.
   * @param prefixedProperty
   *     the full path of the property.
   * @param propertyValue
   *     the string property value.
   * @param componentDescriptor
   *     the component descriptor
   * @param queryComponent
   *     the query component
   * @param context
   *     the context
   * @return the created criteria or null if no criteria necessary.
   */
  @SuppressWarnings("unchecked")
  protected Criteria createIdRestriction(IPropertyDescriptor propertyDescriptor, String prefixedProperty,
                                         Object propertyValue, IComponentDescriptor<?> componentDescriptor,
                                         IQueryComponent queryComponent, Map<String, Object> context) {
    String joinedProperty = prefixedProperty.replace('.' + IEntity.ID, "");
    if (propertyValue instanceof Collection<?>) {
      return Criteria.where(joinedProperty).in((Collection<?>) propertyValue);
    } else if (propertyValue instanceof String) {
      return createStringRestriction(propertyDescriptor, joinedProperty,
          (String) propertyValue, componentDescriptor, queryComponent, context);
    } else {
      return where(joinedProperty).is(propertyValue);
    }
  }

  /**
   * Complete query with translations.
   *
   * @param currentQuery
   *     the current query
   * @param translationsPath
   *     the translations path
   * @param translationsAlias
   *     the translations alias
   * @param property
   *     the property
   * @param propertyDescriptor
   *     the property descriptor
   * @param prefixedProperty
   *     the prefixed property
   * @param locale
   *     the locale
   * @param componentDescriptor
   *     the component descriptor
   * @param queryComponent
   *     the query component
   * @param context
   *     the context
   */
  @SuppressWarnings("unchecked")
  protected void completeQueryWithTranslations(Query currentQuery, String translationsPath, String translationsAlias,
                                               Map.Entry<String, Object> property,
                                               IPropertyDescriptor propertyDescriptor, String prefixedProperty,
                                               Locale locale, IComponentDescriptor<?> componentDescriptor,
                                               IQueryComponent queryComponent, Map<String, Object> context) {
    String propertyValue = (String) property.getValue();
    propertyValue = sanitizeStringValue(propertyValue);
    if (propertyDescriptor instanceof IStringPropertyDescriptor && ((IStringPropertyDescriptor) propertyDescriptor)
        .isTranslatable()) {
      String nlsOrRawValue = null;
      String barePropertyName = property.getKey();
      if (property.getKey().endsWith(IComponentDescriptor.NLS_SUFFIX)) {
        barePropertyName = barePropertyName.substring(0,
            barePropertyName.length() - IComponentDescriptor.NLS_SUFFIX.length());
      } else {
        nlsOrRawValue = propertyValue;
      }
      if (propertyValue != null) {
        List<Criteria> translationRestriction = new ArrayList<>();
        translationRestriction.add(createStringRestriction(
            ((ICollectionPropertyDescriptor<IPropertyTranslation>) componentDescriptor.getPropertyDescriptor(
                translationsPath)).getCollectionDescriptor().getElementDescriptor().getPropertyDescriptor
                (IPropertyTranslation.TRANSLATED_VALUE),
            translationsAlias + "." + IPropertyTranslation.TRANSLATED_VALUE, propertyValue, componentDescriptor,
            queryComponent, context));
        String languagePath = translationsAlias + "." + IPropertyTranslation.LANGUAGE;
        translationRestriction.add(where(languagePath).is(locale.getLanguage()));
        translationRestriction.add(where(translationsAlias + "." + IPropertyTranslation.PROPERTY_NAME).is(barePropertyName));

        List<Criteria> disjunction = new ArrayList<>();
        disjunction.add(new Criteria().andOperator(translationRestriction.toArray(new Criteria[translationRestriction.size()])));
        if (nlsOrRawValue != null) {
          List<Criteria> rawValueRestriction = new ArrayList<>();
          // No SQL exists equivalent in Mongo...
          // rawValueRestriction.add(new Criteria().orOperator(where(translationsPath).is(null), where(languagePath)
          //  .is(locale.getLanguage())));
          String rawPropertyName = barePropertyName + IComponentDescriptor.RAW_SUFFIX;
          rawValueRestriction.add(
              createStringRestriction(componentDescriptor.getPropertyDescriptor(rawPropertyName), rawPropertyName,
                  nlsOrRawValue, componentDescriptor, queryComponent, context));
          if (rawValueRestriction.size() == 1) {
            disjunction.add(rawValueRestriction.get(0));
          } else {
            disjunction.add(new Criteria().andOperator(rawValueRestriction.toArray(new Criteria[rawValueRestriction.size()])));
          }
        }
        currentQuery.addCriteria(new Criteria().orOperator(disjunction.toArray(new Criteria[disjunction.size()])));
      }
    } else {
      completeQuery(currentQuery,
          createStringRestriction(propertyDescriptor, prefixedProperty, propertyValue, componentDescriptor,
              queryComponent, context));
    }
  }

  private String sanitizeStringValue(String propertyValue) {
    String sanitizedValue = propertyValue;
    if (propertyValue != null) {
      for (char specialChar : SPECIAL_CHARS.toCharArray()) {
        String toReplace = "\\" + String.valueOf(specialChar);
        sanitizedValue = sanitizedValue.replaceAll(toReplace, Matcher.quoteReplacement(toReplace));
      }
    }
    return sanitizedValue;
  }

  /**
   * Creates a string based restriction.
   *
   * @param propertyDescriptor
   *     the property descriptor.
   * @param prefixedProperty
   *     the full path of the property.
   * @param propertyValue
   *     the string property value.
   * @param componentDescriptor
   *     the component descriptor
   * @param queryComponent
   *     the query component
   * @param context
   *     the context
   * @return the created criteria or null if no criteria necessary.
   */
  protected Criteria createStringRestriction(IPropertyDescriptor propertyDescriptor, String prefixedProperty,
                                             String propertyValue, IComponentDescriptor<?> componentDescriptor,
                                             IQueryComponent queryComponent, Map<String, Object> context) {
    List<Criteria> disjunctions = new ArrayList<>();
    if (propertyValue.length() > 0) {
      String[] propValues = propertyValue.split(IQueryComponent.DISJUNCT);
      for (String propValue : propValues) {
        String val = propValue;
        if (val.length() > 0) {
          Criteria crit;
          boolean negate = false;
          if (val.startsWith(IQueryComponent.NOT_VAL)) {
            val = val.substring(1);
            negate = true;
          }
          if (IQueryComponent.NULL_VAL.equals(val)) {
            if (negate) {
              crit = where(prefixedProperty).ne(null);
            } else {
              crit = where(prefixedProperty).is(null);
            }
          } else {
            if (IEntity.ID.equals(propertyDescriptor.getName())
                || propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
              if (negate) {
                crit = where(prefixedProperty).ne(val);
              } else {
                crit = where(prefixedProperty).is(val);
              }
            } else {
              crit = createLikeRestriction(propertyDescriptor, prefixedProperty, val, componentDescriptor,
                  queryComponent, context);
            }
          }
          disjunctions.add(crit);
        }
      }
    }
    int disjunctionCount = disjunctions.size();
    if (disjunctionCount == 1) {
      return disjunctions.get(0);
    }
    return new Criteria().orOperator(disjunctions.toArray(new Criteria[disjunctionCount]));
  }

  /**
   * Creates a like restriction.
   *
   * @param propertyDescriptor
   *     the property descriptor.
   * @param prefixedProperty
   *     the complete property path.
   * @param propertyValue
   *     the value to create the like restriction for
   * @param componentDescriptor
   *     the component descriptor
   * @param queryComponent
   *     the query component
   * @param context
   *     the context
   * @return the created criteria or null if no criteria necessary.
   */
  @SuppressWarnings("unused")
  protected Criteria createLikeRestriction(IPropertyDescriptor propertyDescriptor, String prefixedProperty,
                                           String propertyValue, IComponentDescriptor<?> componentDescriptor,
                                           IQueryComponent queryComponent, Map<String, Object> context) {
    String regex = propertyValue;
    if (propertyDescriptor instanceof IStringPropertyDescriptor && ((IStringPropertyDescriptor) propertyDescriptor)
        .isUpperCase()) {
      regex = regex.toUpperCase();
    }
    if (!regex.startsWith("%")) {
      regex = "^" + regex; // make sure that the index is used
    }
    regex = regex.replaceAll("%", ".*");
    if (!regex.endsWith(".*")) {
      regex += ".*";
    }
    return where(prefixedProperty).regex(regex);
  }

  /**
   * Creates a criteria by processing a comparable query structure.
   *
   * @param path
   *     the path to the comparable property.
   * @param queryStructure
   *     the comparable query structure.
   * @param componentDescriptor
   *     the component descriptor
   * @param queryComponent
   *     the query component
   * @param context
   *     the context
   * @return the created criteria or null if no criteria necessary.
   */
  @SuppressWarnings("unused")
  protected Criteria createComparableQueryStructureRestriction(String path, ComparableQueryStructure queryStructure,
                                                               IComponentDescriptor<?> componentDescriptor,
                                                               IQueryComponent queryComponent,
                                                               Map<String, Object> context) {
    Criteria queryStructureRestriction = where(path);
    if (queryStructure.isRestricting()) {
      String comparator = queryStructure.getComparator();
      Object infValue = queryStructure.getInfValue();
      Object supValue = queryStructure.getSupValue();
      Object compareValue = infValue;
      if (compareValue == null) {
        compareValue = supValue;
      }
      switch (comparator) {
        case ComparableQueryStructureDescriptor.EQ:
          queryStructureRestriction.is(compareValue);
          break;
        case ComparableQueryStructureDescriptor.GT:
          queryStructureRestriction.gt(compareValue);
          break;
        case ComparableQueryStructureDescriptor.GE:
          queryStructureRestriction.gte(compareValue);
          break;
        case ComparableQueryStructureDescriptor.LT:
          queryStructureRestriction.lt(compareValue);
          break;
        case ComparableQueryStructureDescriptor.LE:
          queryStructureRestriction.lte(compareValue);
          break;
        case ComparableQueryStructureDescriptor.NU:
          queryStructureRestriction.is(null);
          break;
        case ComparableQueryStructureDescriptor.NN:
          queryStructureRestriction.ne(null);
          break;
        case ComparableQueryStructureDescriptor.BE:
          if (infValue != null && supValue != null) {
            queryStructureRestriction.gte(infValue).andOperator(where(path).lte(supValue));
          } else if (infValue != null) {
            queryStructureRestriction.gte(infValue);
          } else {
            queryStructureRestriction.lte(supValue);
          }
          break;
        default:
          break;
      }
    }
    return queryStructureRestriction;
  }

  /**
   * Whether a query component must be considered empty, thus not generating any
   * restriction.
   *
   * @param queryComponent
   *     the query component to test.
   * @param holdingPropertyDescriptor
   *     the holding property descriptor or null if none.
   * @return true, if the query component does not generate any restriction.
   */
  @SuppressWarnings("UnusedParameters")
  protected boolean isQueryComponentEmpty(IQueryComponent queryComponent,
                                          IPropertyDescriptor holdingPropertyDescriptor) {
    return !queryComponent.isRestricting();
  }

  /**
   * Gets the triStateBooleanSupported.
   *
   * @return the triStateBooleanSupported.
   */
  public boolean isTriStateBooleanSupported() {
    return triStateBooleanSupported;
  }

  /**
   * Sets the triStateBooleanSupported.
   *
   * @param triStateBooleanSupported
   *     the triStateBooleanSupported to set.
   */
  public void setTriStateBooleanSupported(boolean triStateBooleanSupported) {
    this.triStateBooleanSupported = triStateBooleanSupported;
  }

}
