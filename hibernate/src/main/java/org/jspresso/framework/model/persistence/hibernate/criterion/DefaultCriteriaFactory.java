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
package org.jspresso.framework.model.persistence.hibernate.criterion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.application.action.AbstractActionContextAware;
import org.jspresso.framework.application.backend.action.persistence.hibernate.QueryEntitiesAction;
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
 * Default implementation of a criteria factory.
 *
 * @author Vincent Vandenschrick
 */
public class DefaultCriteriaFactory extends AbstractActionContextAware implements ICriteriaFactory {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultCriteriaFactory.class);

  private boolean triStateBooleanSupported;
  private boolean useAliasesForJoins;

  /**
   * Constructs a new {@code DefaultCriteriaFactory} instance.
   */
  public DefaultCriteriaFactory() {
    triStateBooleanSupported = false;
    useAliasesForJoins = false;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("ConstantConditions")
  @Override
  public void completeCriteriaWithOrdering(EnhancedDetachedCriteria criteria, IQueryComponent queryComponent,
                                           Map<String, Object> context) {
    criteria.setProjection(null);
    criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
    // complete sorting properties
    if (queryComponent.getOrderingProperties() != null) {
      for (Map.Entry<String, ESort> orderingProperty : queryComponent.getOrderingProperties().entrySet()) {
        String propertyName = orderingProperty.getKey();
        String[] propElts = propertyName.split("\\.");
        DetachedCriteria orderingCriteria = criteria;
        boolean sortable = true;
        if (propElts.length > 1) {
          IComponentDescriptor<?> currentCompDesc = queryComponent.getQueryDescriptor();
          int i = 0;
          List<String> path = new ArrayList<>();
          for (; sortable && i < propElts.length - 1; i++) {
            IReferencePropertyDescriptor<?> refPropDescriptor = ((IReferencePropertyDescriptor<?>) currentCompDesc
                .getPropertyDescriptor(propElts[i]));
            if (refPropDescriptor != null) {
              sortable = sortable && isSortable(refPropDescriptor);
              if (EntityHelper.isInlineComponentReference(refPropDescriptor)) {
                break;
              }
              currentCompDesc = refPropDescriptor.getReferencedDescriptor();
              path.add(propElts[i]);
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
              for (String pathElt : path) {
                if (isUseAliasesForJoins()) {
                  orderingCriteria = criteria.getSubCriteriaFor(orderingCriteria, pathElt, pathElt,
                      JoinType.LEFT_OUTER_JOIN);
                } else {
                  orderingCriteria = criteria.getSubCriteriaFor(orderingCriteria, pathElt, JoinType.LEFT_OUTER_JOIN);
                }
              }
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
          Order order;
          switch (orderingProperty.getValue()) {
            case DESCENDING:
              order = Order.desc(PropertyHelper.toJavaBeanPropertyName(propertyName));
              break;
            case ASCENDING:
            default:
              order = Order.asc(PropertyHelper.toJavaBeanPropertyName(propertyName));
          }
          orderingCriteria.addOrder(order);
        }
      }
    }
    // Query should always be ordered to preserve pagination.
    criteria.addOrder(Order.desc(IEntity.ID));
  }

  private boolean isSortable(IPropertyDescriptor propertyDescriptor) {
    return propertyDescriptor != null && (!propertyDescriptor.isComputed()
        || propertyDescriptor.getPersistenceFormula() != null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnhancedDetachedCriteria createCriteria(IQueryComponent queryComponent, Map<String, Object> context) {
    EnhancedDetachedCriteria criteria = EnhancedDetachedCriteria.forEntityName(
        queryComponent.getQueryContract().getName());
    boolean abort = completeCriteria(criteria, criteria, null, queryComponent, context);
    if (abort) {
      return null;
    }
    return criteria;
  }

  @SuppressWarnings("ConstantConditions")
  private boolean completeCriteria(EnhancedDetachedCriteria rootCriteria, DetachedCriteria currentCriteria, String path,
                                   IQueryComponent aQueryComponent, Map<String, Object> context) {
    boolean abort = false;
    IComponentDescriptor<?> componentDescriptor = aQueryComponent.getQueryDescriptor();
    if (aQueryComponent instanceof ComparableQueryStructure) {
      completeCriteria(currentCriteria, createComparableQueryStructureRestriction(path,
          (ComparableQueryStructure) aQueryComponent, componentDescriptor, aQueryComponent, context));
    } else {
      String translationsPath = AbstractComponentDescriptor.getComponentTranslationsDescriptorTemplate().getName();
      String translationsAlias = currentCriteria.getAlias() + "_" + componentDescriptor.getComponentContract().getSimpleName() + "_" + translationsPath;
      if (componentDescriptor.isTranslatable()) {
        rootCriteria.getSubCriteriaFor(currentCriteria, translationsPath, translationsAlias, JoinType.LEFT_OUTER_JOIN);
      }
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
            String prefixedProperty = PropertyHelper.toJavaBeanPropertyName(propertyName);
            if (path != null) {
              prefixedProperty = path + "." + prefixedProperty;
            }
            if (propertyValue instanceof IEntity) {
              if (!((IEntity) propertyValue).isPersistent()) {
                abort = true;
              } else {
                completeCriteria(currentCriteria, Restrictions.eq(prefixedProperty, propertyValue));
              }
            } else if (propertyValue instanceof Boolean && (isTriStateBooleanSupported() || (Boolean) propertyValue)) {
              completeCriteria(currentCriteria, Restrictions.eq(prefixedProperty, propertyValue));
            } else if(IEntity.ID.equalsIgnoreCase(propertyName)) {
              completeCriteria(currentCriteria,
                  createIdRestriction(propertyDescriptor, prefixedProperty, propertyValue, componentDescriptor,
                      aQueryComponent, context));
            } else if (propertyValue instanceof String) {
              completeCriteriaWithTranslations(currentCriteria, translationsPath, translationsAlias, property,
                  propertyDescriptor, prefixedProperty, getBackendController(context).getLocale(), componentDescriptor,
                  aQueryComponent, context);
            } else if (propertyValue instanceof Number || propertyValue instanceof Date) {
              completeCriteria(currentCriteria, Restrictions.eq(prefixedProperty, propertyValue));
            } else if (propertyValue instanceof EnumQueryStructure) {
              completeCriteria(currentCriteria, createEnumQueryStructureRestriction(prefixedProperty,
                  ((EnumQueryStructure) propertyValue)));
            } else if (propertyValue instanceof IQueryComponent) {
              IQueryComponent joinedComponent = ((IQueryComponent) propertyValue);
              if (!isQueryComponentEmpty(joinedComponent, propertyDescriptor)) {
                if (joinedComponent.isInlineComponent()/* || path != null */) {
                  // the joined component is an inline component so we must use
                  // dot nested properties. Same applies if we are in a nested
                  // path i.e. already on an inline component.
                  abort = abort || completeCriteria(rootCriteria, currentCriteria, prefixedProperty,
                      (IQueryComponent) propertyValue, context);
                } else {
                  // the joined component is an entity so we must use
                  // nested criteria; unless the autoComplete property
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
                        Criterion crit = Restrictions.isNull(prefixedProperty);
                        if (negate) {
                          crit = Restrictions.not(crit);
                        }
                        completeCriteria(currentCriteria, crit);
                        digDeeper = false;
                      }
                    }
                  }
                  if (digDeeper) {
                    DetachedCriteria joinCriteria;
                    if (isUseAliasesForJoins()) {
                      joinCriteria = rootCriteria.getSubCriteriaFor(currentCriteria, prefixedProperty, prefixedProperty,
                          JoinType.INNER_JOIN);
                    } else {
                      joinCriteria = rootCriteria.getSubCriteriaFor(currentCriteria, prefixedProperty,
                          JoinType.INNER_JOIN);
                    }
                    abort = abort || completeCriteria(rootCriteria, joinCriteria, null, joinedComponent, context);
                  }
                }
              }
            } else if (propertyValue != null) {
              // Unknown property type. Assume equals.
              completeCriteria(currentCriteria, Restrictions.eq(prefixedProperty, propertyValue));
            }
          }
        }
      }
    }
    return abort;
  }

  /**
   * Complete criteria with translations.
   *
   * @param currentCriteria
   *     the current criteria
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
  protected void completeCriteriaWithTranslations(DetachedCriteria currentCriteria, String translationsPath,
                                                  String translationsAlias, Map.Entry<String, Object> property,
                                                  IPropertyDescriptor propertyDescriptor, String prefixedProperty,
                                                  Locale locale, IComponentDescriptor<?> componentDescriptor,
                                                  IQueryComponent queryComponent, Map<String, Object> context) {
    if (propertyDescriptor instanceof IStringPropertyDescriptor && ((IStringPropertyDescriptor) propertyDescriptor)
        .isTranslatable()) {
      String nlsOrRawValue = null;
      String nlsValue = (String) property.getValue();
      String barePropertyName = property.getKey();
      if (property.getKey().endsWith(IComponentDescriptor.NLS_SUFFIX)) {
        barePropertyName = barePropertyName.substring(0,
            barePropertyName.length() - IComponentDescriptor.NLS_SUFFIX.length());
      } else {
        nlsOrRawValue = nlsValue;
      }
      if (nlsValue != null) {
        Junction translationRestriction = Restrictions.conjunction();
        translationRestriction.add(createStringRestriction(
            ((ICollectionPropertyDescriptor<IPropertyTranslation>) componentDescriptor.getPropertyDescriptor(
                translationsPath)).getCollectionDescriptor().getElementDescriptor().getPropertyDescriptor(
                IPropertyTranslation.TRANSLATED_VALUE), translationsAlias + "." + IPropertyTranslation.TRANSLATED_VALUE,
            nlsValue, componentDescriptor, queryComponent, context));
        String languagePath = translationsAlias + "." + IPropertyTranslation.LANGUAGE;
        translationRestriction.add(Restrictions.eq(languagePath, locale.getLanguage()));
        translationRestriction.add(Restrictions.eq(translationsAlias + "." + IPropertyTranslation.PROPERTY_NAME,
            barePropertyName));

        Junction disjunction = Restrictions.disjunction();
        disjunction.add(translationRestriction);
        if (nlsOrRawValue != null) {
          Junction rawValueRestriction = Restrictions.conjunction();
          rawValueRestriction.add(Restrictions.disjunction().add(Restrictions.isNull(languagePath)).add(Restrictions.ne(
              languagePath, locale.getLanguage())));
          String rawPropertyName = barePropertyName + IComponentDescriptor.RAW_SUFFIX;
          rawValueRestriction.add(createStringRestriction(componentDescriptor.getPropertyDescriptor(rawPropertyName),
              rawPropertyName, nlsOrRawValue, componentDescriptor, queryComponent, context));
          disjunction.add(rawValueRestriction);
        }
        currentCriteria.add(disjunction);
      }
    } else {
      completeCriteria(currentCriteria, createStringRestriction(propertyDescriptor, prefixedProperty,
          (String) property.getValue(), componentDescriptor, queryComponent, context));
    }
  }

  /**
   * Complete with criterion.
   *
   * @param currentCriteria
   *     the current criteria
   * @param criterion
   *     the criterion
   */
  protected void completeCriteria(DetachedCriteria currentCriteria, Criterion criterion) {
    if (criterion != null) {
      currentCriteria.add(criterion);
    }
  }

  /**
   * Complements a criteria by processing an enumeration query structure.
   *
   * @param path
   *     the path to the comparable property.
   * @param enumQueryStructure
   *     the collection of checked / unchecked enumeration values.
   * @return the created criterion or null if no criterion necessary.
   */
  protected Criterion createEnumQueryStructureRestriction(String path, EnumQueryStructure enumQueryStructure) {
    Set<String> inListValues = new HashSet<>();
    boolean nullAllowed = false;
    for (EnumValueQueryStructure inListValue : enumQueryStructure.getSelectedEnumerationValues()) {
      if (inListValue.getValue() == null || "".equals(inListValue.getValue())) {
        nullAllowed = true;
      } else {
        inListValues.add(inListValue.getValue());
      }
    }
    Junction queryStructureRestriction = null;
    if (!inListValues.isEmpty()) {
      queryStructureRestriction = Restrictions.disjunction();
      queryStructureRestriction.add(Restrictions.in(path, inListValues));
      if (nullAllowed) {
        queryStructureRestriction.add(Restrictions.isNull(path));
      }
    }
    return queryStructureRestriction;
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
   * @return the created criterion or null if no criterion necessary.
   */
  @SuppressWarnings("unchecked")
  protected Criterion createIdRestriction(IPropertyDescriptor propertyDescriptor, String prefixedProperty,
                                          Object propertyValue, IComponentDescriptor<?> componentDescriptor,
                                          IQueryComponent queryComponent, Map<String, Object> context) {
    if (propertyValue instanceof Collection<?>) {
      return QueryEntitiesAction.createEntityIdsInCriterion((Collection<Serializable>) propertyValue, 100);
    } else {
      if (propertyValue instanceof String) {
        return createStringRestriction(propertyDescriptor, prefixedProperty, (String) propertyValue, componentDescriptor,
            queryComponent, context);
      } else {
        return Restrictions.eq(prefixedProperty, propertyValue);
      }
    }
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
   * @return the created criterion or null if no criterion necessary.
   */
  protected Criterion createStringRestriction(IPropertyDescriptor propertyDescriptor, String prefixedProperty,
                                              String propertyValue, IComponentDescriptor<?> componentDescriptor,
                                              IQueryComponent queryComponent, Map<String, Object> context) {
    Junction stringRestriction = null;
    if (propertyValue.length() > 0) {
      String[] propValues = propertyValue.split(IQueryComponent.DISJUNCT);
      stringRestriction = Restrictions.disjunction();
      for (String propValue : propValues) {
        String val = propValue;
        if (val.length() > 0) {
          Criterion crit;
          boolean negate = false;
          if (val.startsWith(IQueryComponent.NOT_VAL)) {
            val = val.substring(1);
            negate = true;
          }
          if (IQueryComponent.NULL_VAL.equals(val)) {
            crit = Restrictions.isNull(prefixedProperty);
          } else {
            if (IEntity.ID.equals(propertyDescriptor.getName())
                || propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
              crit = Restrictions.eq(prefixedProperty, val);
            } else {
              crit = createLikeRestriction(propertyDescriptor, prefixedProperty, val, componentDescriptor,
                  queryComponent, context);
            }
          }
          if (negate) {
            crit = Restrictions.not(crit);
          }
          stringRestriction.add(crit);
        }
      }
    }
    return stringRestriction;
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
   * @return the created criterion or null if no criterion necessary.
   */
  protected Criterion createLikeRestriction(IPropertyDescriptor propertyDescriptor, String prefixedProperty,
                                            String propertyValue, IComponentDescriptor<?> componentDescriptor,
                                            IQueryComponent queryComponent, Map<String, Object> context) {
    MatchMode matchMode;
    if (propertyValue.contains("%")) {
      matchMode = MatchMode.EXACT;
    } else {
      matchMode = MatchMode.START;
    }
    if (propertyDescriptor instanceof IStringPropertyDescriptor && ((IStringPropertyDescriptor) propertyDescriptor)
        .isUpperCase()) {
      // don't use ignoreCase() to be able to leverage indices.
      return Restrictions.like(prefixedProperty, propertyValue.toUpperCase(), matchMode);
    }
    return Restrictions.like(prefixedProperty, propertyValue, matchMode).ignoreCase();
  }

  /**
   * Creates a criterion by processing a comparable query structure.
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
   * @return the created criterion or null if no criterion necessary.
   */
  protected Criterion createComparableQueryStructureRestriction(String path, ComparableQueryStructure queryStructure,
                                                                IComponentDescriptor<?> componentDescriptor,
                                                                IQueryComponent queryComponent,
                                                                Map<String, Object> context) {
    Junction queryStructureRestriction = null;
    if (queryStructure.isRestricting()) {
      queryStructureRestriction = Restrictions.conjunction();
      String comparator = queryStructure.getComparator();
      Object infValue = queryStructure.getInfValue();
      Object supValue = queryStructure.getSupValue();
      Object compareValue = infValue;
      if (compareValue == null) {
        compareValue = supValue;
      }
      switch (comparator) {
        case ComparableQueryStructureDescriptor.EQ:
          queryStructureRestriction.add(Restrictions.eq(path, compareValue));
          break;
        case ComparableQueryStructureDescriptor.GT:
          queryStructureRestriction.add(Restrictions.gt(path, compareValue));
          break;
        case ComparableQueryStructureDescriptor.GE:
          queryStructureRestriction.add(Restrictions.ge(path, compareValue));
          break;
        case ComparableQueryStructureDescriptor.LT:
          queryStructureRestriction.add(Restrictions.lt(path, compareValue));
          break;
        case ComparableQueryStructureDescriptor.LE:
          queryStructureRestriction.add(Restrictions.le(path, compareValue));
          break;
        case ComparableQueryStructureDescriptor.NU:
          queryStructureRestriction.add(Restrictions.isNull(path));
          break;
        case ComparableQueryStructureDescriptor.NN:
          queryStructureRestriction.add(Restrictions.isNotNull(path));
          break;
        case ComparableQueryStructureDescriptor.BE:
          if (infValue != null && supValue != null) {
            queryStructureRestriction.add(Restrictions.between(path, infValue, supValue));
          } else if (infValue != null) {
            queryStructureRestriction.add(Restrictions.ge(path, infValue));
          } else {
            queryStructureRestriction.add(Restrictions.le(path, supValue));
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
  protected boolean isTriStateBooleanSupported() {
    return triStateBooleanSupported;
  }

  /**
   * Configures the criteria factory whether to consider use 3-states booleans, i.e. true, false or undefined. If
   * <strong>triStateBooleanSupported</strong> is set to false, then a {@code false} boolean value will simply be
   * ignored in the generated criteria.
   *
   * @param triStateBooleanSupported
   *     the triStateBooleanSupported to set.
   */
  public void setTriStateBooleanSupported(boolean triStateBooleanSupported) {
    this.triStateBooleanSupported = triStateBooleanSupported;
  }

  /**
   * Is use aliases for joins.
   *
   * @return the boolean
   */
  protected boolean isUseAliasesForJoins() {
    return useAliasesForJoins;
  }

  /**
   * Sets use aliases for joins.
   *
   * @param useAliasesForJoins
   *     the use aliases for joins
   */
  public void setUseAliasesForJoins(boolean useAliasesForJoins) {
    this.useAliasesForJoins = useAliasesForJoins;
  }
}
