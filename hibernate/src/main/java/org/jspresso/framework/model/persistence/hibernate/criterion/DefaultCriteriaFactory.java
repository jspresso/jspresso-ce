/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.ComparableQueryStructure;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.collection.ESort;
import org.jspresso.framework.view.descriptor.basic.PropertyDescriptorHelper;

/**
 * Default implementation of a criteria factory.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultCriteriaFactory implements ICriteriaFactory {

  /**
   * {@inheritDoc}
   */
  public void completeCriteriaWithOrdering(EnhancedDetachedCriteria criteria,
      IQueryComponent queryComponent) {
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
              .getComponentDescriptor();
          int i = 0;
          List<String> path = new ArrayList<String>();
          for (; sortable && i < propElts.length - 1; i++) {
            IReferencePropertyDescriptor<?> refPropDescriptor = ((IReferencePropertyDescriptor<?>) currentCompDesc
                .getPropertyDescriptor(propElts[i]));
            sortable = sortable && isSortable(refPropDescriptor);
            IComponentDescriptor<?> referencedDesc = refPropDescriptor
                .getReferencedDescriptor();
            if (!IEntity.class.isAssignableFrom(referencedDesc
                .getComponentContract()) && !referencedDesc.isPurelyAbstract()) {
              break;
            }
            currentCompDesc = referencedDesc;
            path.add(propElts[i]);
          }
          if (sortable) {
            StringBuffer name = new StringBuffer();
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
                    pathElt, CriteriaSpecification.LEFT_JOIN);
              }
              propertyName = name.toString();
            }
          }
        } else {
          sortable = isSortable(queryComponent.getComponentDescriptor()
              .getPropertyDescriptor(propertyName));
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
  }

  private boolean isSortable(IPropertyDescriptor propertyDescriptor) {
    return !propertyDescriptor.isComputed()
        || propertyDescriptor.getPersistenceFormula() != null;
  }

  /**
   * {@inheritDoc}
   */
  public EnhancedDetachedCriteria createCriteria(IQueryComponent queryComponent) {
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
    if (ComparableQueryStructure.class.isAssignableFrom(aQueryComponent
        .getQueryContract())) {
      String comparator = (String) aQueryComponent
          .get(ComparableQueryStructureDescriptor.COMPARATOR);
      Object infValue = aQueryComponent
          .get(ComparableQueryStructureDescriptor.INF_VALUE);
      Object supValue = aQueryComponent
          .get(ComparableQueryStructureDescriptor.SUP_VALUE);
      if (infValue != null || supValue != null) {
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
    } else {
      IComponentDescriptor<?> componentDescriptor = aQueryComponent
          .getComponentDescriptor();
      for (Map.Entry<String, Object> property : aQueryComponent.entrySet()) {
        if (componentDescriptor.getPropertyDescriptor(property.getKey()) != null) {
          if (!PropertyDescriptorHelper.isComputed(componentDescriptor,
              property.getKey())) {
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
                && ((Boolean) property.getValue()).booleanValue()) {
              currentCriteria.add(Restrictions.eq(prefixedProperty,
                  property.getValue()));
            } else if (property.getValue() instanceof String) {
              String propValue = (String) property.getValue();
              if (propValue.length() > 0) {
                String[] propValues = propValue.split(";");
                Junction disjunction = Restrictions.disjunction();
                currentCriteria.add(disjunction);
                for (int i = 0; i < propValues.length; i++) {
                  String val = propValues[i];
                  if (val.length() > 0) {
                    disjunction.add(Restrictions.like(prefixedProperty, val,
                        MatchMode.START).ignoreCase());
                  }
                }
              }
            } else if (property.getValue() instanceof Number
                || property.getValue() instanceof Date) {
              currentCriteria.add(Restrictions.eq(prefixedProperty,
                  property.getValue()));
            } else if (property.getValue() instanceof IQueryComponent) {
              IQueryComponent joinedComponent = ((IQueryComponent) property
                  .getValue());
              if (!isQueryComponentEmpty(joinedComponent)) {
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
                  // nested criteria.
                  DetachedCriteria joinCriteria = rootCriteria
                      .getSubCriteriaFor(currentCriteria, prefixedProperty,
                          CriteriaSpecification.INNER_JOIN);
                  abort = abort
                      || completeCriteria(rootCriteria, joinCriteria, null,
                          joinedComponent);
                }
              }
            }
          }
        }
      }
    }
    return abort;
  }

  private boolean isQueryComponentEmpty(IQueryComponent queryComponent) {
    if (queryComponent == null || queryComponent.isEmpty()) {
      return true;
    }
    IComponentDescriptor<?> componentDescriptor = queryComponent
        .getComponentDescriptor();
    for (Map.Entry<String, Object> property : queryComponent.entrySet()) {
      if (componentDescriptor.getPropertyDescriptor(property.getKey()) != null) {
        if (property.getValue() != null) {
          if (property.getValue() instanceof IQueryComponent) {
            if (!isQueryComponentEmpty((IQueryComponent) property.getValue())) {
              return false;
            }
          } else {
            return false;
          }
        }
      }
    }
    return true;
  }

}
