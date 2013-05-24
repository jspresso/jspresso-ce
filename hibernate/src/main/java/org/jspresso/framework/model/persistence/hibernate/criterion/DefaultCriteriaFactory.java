/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.jspresso.framework.application.action.AbstractActionContextAware;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.ComparableQueryStructure;
import org.jspresso.framework.model.component.query.EnumQueryStructure;
import org.jspresso.framework.model.component.query.EnumValueQueryStructure;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;
import org.jspresso.framework.model.entity.EntityHelper;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.view.descriptor.basic.PropertyViewDescriptorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of a criteria factory.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultCriteriaFactory extends AbstractActionContextAware
    implements ICriteriaFactory {

  private static final Logger LOG = LoggerFactory
                                      .getLogger(DefaultCriteriaFactory.class);

  private boolean             triStateBooleanSupported;

  /**
   * Constructs a new <code>DefaultCriteriaFactory</code> instance.
   */
  public DefaultCriteriaFactory() {
    triStateBooleanSupported = false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void completeCriteriaWithOrdering(EnhancedDetachedCriteria criteria,
      IQueryComponent queryComponent, Map<String, Object> context) {
    criteria.setProjection(null);
    criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
    // complete sorting properties
    if (queryComponent.getOrderingProperties() != null) {
      for (Map.Entry<String, ESort> orderingProperty : queryComponent
          .getOrderingProperties().entrySet()) {
        String propertyName = orderingProperty.getKey();
        String[] propElts = propertyName.split("\\.");
        DetachedCriteria orderingCriteria = criteria;
        boolean sortable = true;
        if (propElts.length > 1) {
          IComponentDescriptor<?> currentCompDesc = queryComponent
              .getQueryDescriptor();
          int i = 0;
          List<String> path = new ArrayList<String>();
          for (; sortable && i < propElts.length - 1; i++) {
            IReferencePropertyDescriptor<?> refPropDescriptor = ((IReferencePropertyDescriptor<?>) currentCompDesc
                .getPropertyDescriptor(propElts[i]));
            if (refPropDescriptor != null) {
              sortable = sortable && isSortable(refPropDescriptor);
              if (EntityHelper.isInlineComponentReference(refPropDescriptor)) {
                break;
              }
              IComponentDescriptor<?> referencedDesc = refPropDescriptor
                  .getReferencedDescriptor();
              currentCompDesc = referencedDesc;
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
              IPropertyDescriptor propDescriptor = currentCompDesc
                  .getPropertyDescriptor(propElts[j]);
              sortable = sortable && isSortable(propDescriptor);
              if (j < propElts.length - 1) {
                currentCompDesc = ((IReferencePropertyDescriptor<?>) propDescriptor)
                    .getReferencedDescriptor();
              }
              if (j > i) {
                name.append(".");
              }
              name.append(propElts[j]);
            }
            if (sortable) {
              for (String pathElt : path) {
                orderingCriteria = criteria.getSubCriteriaFor(orderingCriteria,
                    pathElt, JoinType.LEFT_OUTER_JOIN);
              }
              propertyName = name.toString();
            }
          }
        } else {
          IPropertyDescriptor propertyDescriptor = queryComponent
              .getQueryDescriptor().getPropertyDescriptor(propertyName);
          if (propertyDescriptor != null) {
            sortable = isSortable(propertyDescriptor);
          } else {
            LOG.error("Ordering property {} not found on {}", propertyName,
                queryComponent.getQueryDescriptor().getComponentContract()
                    .getName());
            sortable = false;
          }
        }
        if (sortable) {
          Order order;
          switch (orderingProperty.getValue()) {
            case DESCENDING:
              order = Order.desc(propertyName);
              break;
            case ASCENDING:
            default:
              order = Order.asc(propertyName);
          }
          orderingCriteria.addOrder(order);
        }
      }
    }
    // Query should always be ordered to preserve pagination.
    criteria.addOrder(Order.desc(IEntity.ID));
  }

  private boolean isSortable(IPropertyDescriptor propertyDescriptor) {
    return propertyDescriptor != null
        && (!propertyDescriptor.isComputed() || propertyDescriptor
            .getPersistenceFormula() != null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EnhancedDetachedCriteria createCriteria(
      IQueryComponent queryComponent, Map<String, Object> context) {
    EnhancedDetachedCriteria criteria = EnhancedDetachedCriteria
        .forEntityName(queryComponent.getQueryContract().getName());
    boolean abort = completeCriteria(criteria, criteria, null, queryComponent);
    if (abort) {
      return null;
    }
    return criteria;
  }

  private boolean completeCriteria(EnhancedDetachedCriteria rootCriteria,
      DetachedCriteria currentCriteria, String path,
      IQueryComponent aQueryComponent) {
    boolean abort = false;
    if (aQueryComponent instanceof ComparableQueryStructure) {
      completeWithComparableQueryStructure(currentCriteria, path,
          (ComparableQueryStructure) aQueryComponent);
    } else {
      IComponentDescriptor<?> componentDescriptor = aQueryComponent
          .getQueryDescriptor();
      for (Map.Entry<String, Object> property : aQueryComponent.entrySet()) {
        IPropertyDescriptor propertyDescriptor = componentDescriptor
            .getPropertyDescriptor(property.getKey());
        if (propertyDescriptor != null) {
          boolean isEntityRef = false;
          if (componentDescriptor.isEntity()
              && aQueryComponent.containsKey(IEntity.ID)) {
            isEntityRef = true;
          }
          if (!PropertyViewDescriptorHelper.isComputed(componentDescriptor,
              property.getKey())
              && (!isEntityRef || IEntity.ID.equals(property.getKey()))) {
            String prefixedProperty;
            if (path != null) {
              prefixedProperty = path + "." + property.getKey();
            } else {
              prefixedProperty = property.getKey();
            }
            if (property.getValue() instanceof IEntity) {
              if (!((IEntity) property.getValue()).isPersistent()) {
                abort = true;
              } else {
                currentCriteria.add(Restrictions.eq(prefixedProperty,
                    property.getValue()));
              }
            } else if (property.getValue() instanceof Boolean
                && (isTriStateBooleanSupported() || ((Boolean) property
                    .getValue()).booleanValue())) {
              currentCriteria.add(Restrictions.eq(prefixedProperty,
                  property.getValue()));
            } else if (property.getValue() instanceof String) {
              if (IEntity.ID.equalsIgnoreCase(property.getKey())) {
                createIdRestriction(propertyDescriptor, currentCriteria,
                    property.getValue(), prefixedProperty);
              } else {
                createStringRestriction(propertyDescriptor, currentCriteria,
                    (String) property.getValue(), prefixedProperty);
              }
            } else if (property.getValue() instanceof Number
                || property.getValue() instanceof Date) {
              currentCriteria.add(Restrictions.eq(prefixedProperty,
                  property.getValue()));
            } else if (property.getValue() instanceof EnumQueryStructure) {
              completeWithEnumQueryStructure(currentCriteria, prefixedProperty,
                  ((EnumQueryStructure) property.getValue()));
            } else if (property.getValue() instanceof IQueryComponent) {
              IQueryComponent joinedComponent = ((IQueryComponent) property
                  .getValue());
              if (!isQueryComponentEmpty(joinedComponent, propertyDescriptor)) {
                if (joinedComponent.isInlineComponent()/* || path != null */) {
                  // the joined component is an inlined component so we must use
                  // dot nested properties. Same applies if we are in a nested
                  // path i.e. already on an inline component.
                  abort = abort
                      || completeCriteria(rootCriteria, currentCriteria,
                          prefixedProperty,
                          (IQueryComponent) property.getValue());
                } else {
                  // the joined component is an entity so we must use
                  // nested criteria; unless the autoComplete property
                  // is a special char.
                  boolean digDeeper = true;
                  String autoCompleteProperty = joinedComponent
                      .getQueryDescriptor().getAutoCompleteProperty();
                  if (autoCompleteProperty != null) {
                    String val = (String) joinedComponent
                        .get(autoCompleteProperty);
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
                        currentCriteria.add(crit);
                        digDeeper = false;
                      }
                    }
                  }
                  if (digDeeper) {
                    DetachedCriteria joinCriteria = rootCriteria
                        .getSubCriteriaFor(currentCriteria, prefixedProperty,
                            JoinType.INNER_JOIN);
                    abort = abort
                        || completeCriteria(rootCriteria, joinCriteria, null,
                            joinedComponent);
                  }
                }
              }
            } else if (property.getValue() != null) {
              // Unknown property type. Assume equals.
              currentCriteria.add(Restrictions.eq(prefixedProperty,
                  property.getValue()));
            }
          }
        }
      }
    }
    return abort;
  }

  /**
   * Complements a criteria by processing an enumeration query structure.
   * 
   * @param currentCriteria
   *          the current criteria that is being built.
   * @param path
   *          the path to the comparable property.
   * @param enumQueryStructure
   *          the collection of checked / unchecked enumeration values.
   */
  protected void completeWithEnumQueryStructure(
      DetachedCriteria currentCriteria, String path,
      EnumQueryStructure enumQueryStructure) {
    Set<String> inListValues = new HashSet<String>();
    boolean nullAllowed = false;
    for (EnumValueQueryStructure inListValue : enumQueryStructure
        .getSelectedEnumerationValues()) {
      if (inListValue.getValue() == null || "".equals(inListValue.getValue())) {
        nullAllowed = true;
      } else {
        inListValues.add(inListValue.getValue());
      }
    }
    if (!inListValues.isEmpty()) {
      Criterion inListCriterion = Restrictions.in(path, inListValues);
      if (nullAllowed) {
        Junction disjunction = Restrictions.disjunction();
        disjunction.add(inListCriterion);
        disjunction.add(Restrictions.isNull(path));
        currentCriteria.add(disjunction);
      } else {
        currentCriteria.add(inListCriterion);
      }
    }
  }

  /**
   * Creates an id based restriction.
   * 
   * @param propertyDescriptor
   *          the id property descriptor.
   * @param currentCriteria
   *          the current criteria being built.
   * @param propertyValue
   *          the string property value.
   * @param prefixedProperty
   *          the full path of the property.
   */
  protected void createIdRestriction(IPropertyDescriptor propertyDescriptor,
      DetachedCriteria currentCriteria, Object propertyValue,
      String prefixedProperty) {
    if (propertyValue instanceof String) {
      createStringRestriction(propertyDescriptor, currentCriteria,
          (String) propertyValue, prefixedProperty);
    } else {
      currentCriteria.add(Restrictions.eq(prefixedProperty, propertyValue));
    }
  }

  /**
   * Creates a string based restriction.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param currentCriteria
   *          the current criteria being built.
   * @param propertyValue
   *          the string property value.
   * @param prefixedProperty
   *          the full path of the property.
   */
  protected void createStringRestriction(
      IPropertyDescriptor propertyDescriptor, DetachedCriteria currentCriteria,
      String propertyValue, String prefixedProperty) {
    if (propertyValue.length() > 0) {
      String[] propValues = propertyValue.split(IQueryComponent.DISJUNCT);
      Junction disjunction = Restrictions.disjunction();
      currentCriteria.add(disjunction);
      for (int i = 0; i < propValues.length; i++) {
        String val = propValues[i];
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
              crit = createLikeRestriction(propertyDescriptor,
                  prefixedProperty, val);
            }
          }
          if (negate) {
            crit = Restrictions.not(crit);
          }
          disjunction.add(crit);
        }
      }
    }
  }

  /**
   * Creates a like restriction.
   * 
   * @param propertyDescriptor
   *          the property descriptor.
   * @param prefixedProperty
   *          the complete property path.
   * @param val
   *          the value to create the like restriction for
   * @return the like criterion;
   */
  protected Criterion createLikeRestriction(
      IPropertyDescriptor propertyDescriptor, String prefixedProperty,
      String val) {
    if (propertyDescriptor instanceof IStringPropertyDescriptor
        && ((IStringPropertyDescriptor) propertyDescriptor).isUpperCase()) {
      // don't use ignoreCase() to be able to leverage indices.
      return Restrictions.like(prefixedProperty, val.toUpperCase(),
          MatchMode.START);
    }
    return Restrictions.like(prefixedProperty, val, MatchMode.START)
        .ignoreCase();
  }

  /**
   * Complements a criteria by processing a comparable query structure.
   * 
   * @param currentCriteria
   *          the current criteria that is being built.
   * @param path
   *          the path to the comparable property.
   * @param queryStructure
   *          the comparable query structure.
   */
  protected void completeWithComparableQueryStructure(
      DetachedCriteria currentCriteria, String path,
      ComparableQueryStructure queryStructure) {
    if (queryStructure.isRestricting()) {
      String comparator = queryStructure.getComparator();
      Object infValue = queryStructure.getInfValue();
      Object supValue = queryStructure.getSupValue();
      Object compareValue = infValue;
      if (compareValue == null) {
        compareValue = supValue;
      }
      if (ComparableQueryStructureDescriptor.EQ.equals(comparator)) {
        currentCriteria.add(Restrictions.eq(path, compareValue));
      } else if (ComparableQueryStructureDescriptor.GT.equals(comparator)) {
        currentCriteria.add(Restrictions.gt(path, compareValue));
      } else if (ComparableQueryStructureDescriptor.GE.equals(comparator)) {
        currentCriteria.add(Restrictions.ge(path, compareValue));
      } else if (ComparableQueryStructureDescriptor.LT.equals(comparator)) {
        currentCriteria.add(Restrictions.lt(path, compareValue));
      } else if (ComparableQueryStructureDescriptor.LE.equals(comparator)) {
        currentCriteria.add(Restrictions.le(path, compareValue));
      } else if (ComparableQueryStructureDescriptor.BE.equals(comparator)) {
        if (infValue != null && supValue != null) {
          currentCriteria.add(Restrictions.between(path, infValue, supValue));
        } else if (infValue != null) {
          currentCriteria.add(Restrictions.ge(path, infValue));
        } else {
          currentCriteria.add(Restrictions.le(path, supValue));
        }
      }
    }
  }

  /**
   * Wether a query component must be considered empty, thus not generating any
   * restriction.
   * 
   * @param queryComponent
   *          the query component to test.
   * @param holdingPropertyDescriptor
   *          the holding property descriptor or null if none.
   * @return true, if the query component does not generate any restriction.
   */
  protected boolean isQueryComponentEmpty(IQueryComponent queryComponent,
      IPropertyDescriptor holdingPropertyDescriptor) {
    if (queryComponent == null || queryComponent.isEmpty()) {
      return true;
    }
    IComponentDescriptor<?> componentDescriptor = queryComponent
        .getQueryDescriptor();
    for (Map.Entry<String, Object> property : queryComponent.entrySet()) {
      IPropertyDescriptor propertyDescriptor = componentDescriptor
          .getPropertyDescriptor(property.getKey());
      if (propertyDescriptor != null) {
        if (property.getValue() != null) {
          if (property.getValue() instanceof ComparableQueryStructure) {
            if (((ComparableQueryStructure) property.getValue())
                .isRestricting()) {
              return false;
            }
          } else if (property.getValue() instanceof EnumQueryStructure) {
            if (!((EnumQueryStructure) property.getValue()).isEmpty()) {
              return false;
            }
          } else if (property.getValue() instanceof IQueryComponent) {
            if (!isQueryComponentEmpty((IQueryComponent) property.getValue(),
                propertyDescriptor)) {
              return false;
            }
          } else {
            // I can't understand the reason of the following code.
            // We are exploring a sub-QueryComponent to determine if it should
            // imply a restriction. Whenever the sub-QueryComponent only
            // contains properties coming from the initialization mapping,
            // we should still consider it as non-emty. If we don't, we may miss
            // restrictions that are imposed by the user using nested properties
            // or LOV.

            // Map<String, Object> initializationMapping = null;
            // if (holdingPropertyDescriptor instanceof
            // IReferencePropertyDescriptor<?>) {
            // initializationMapping = ((IReferencePropertyDescriptor<?>)
            // holdingPropertyDescriptor)
            // .getInitializationMapping();
            // }
            // if (initializationMapping == null
            // || !initializationMapping.containsKey(property.getKey())) {
            // return false;
            // }
            return false;
          }
        }
      }
    }
    return true;
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
   *          the triStateBooleanSupported to set.
   */
  public void setTriStateBooleanSupported(boolean triStateBooleanSupported) {
    this.triStateBooleanSupported = triStateBooleanSupported;
  }

}
