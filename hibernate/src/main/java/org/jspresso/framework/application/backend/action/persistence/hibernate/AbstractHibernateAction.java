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
package org.jspresso.framework.application.backend.action.persistence.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.jspresso.framework.application.backend.action.AbstractBackendAction;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * This the root abstract class of all hibernate related persistence actions.
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
 */
public abstract class AbstractHibernateAction extends AbstractBackendAction {

  /**
   * Performs necessary cleanings when an entity is deleted.
   * 
   * @param entity
   *          the deleted entity.
   * @param context
   *          The action context.
   * @param dryRun
   *          set to true to simulate before actually doing it.
   * @throws IllegalAccessException
   *           whenever this kind of exception occurs.
   * @throws InvocationTargetException
   *           whenever this kind of exception occurs.
   * @throws NoSuchMethodException
   *           whenever this kind of exception occurs.
   */
  @SuppressWarnings("unchecked")
  protected void cleanRelationshipsOnDeletion(IEntity entity,
      Map<String, Object> context, boolean dryRun)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    try {
      entity.setPropertyProcessorsEnabled(false);
      IComponentDescriptor entityDescriptor = getEntityFactory(context)
          .getComponentDescriptor(entity.getContract());
      for (Map.Entry<String, Object> property : entity.straightGetProperties()
          .entrySet()) {
        if (property.getValue() != null) {
          IPropertyDescriptor propertyDescriptor = entityDescriptor
              .getPropertyDescriptor(property.getKey());
          if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
            if (dryRun && property.getValue() != null) {
              // manually trigger reverse relations preprocessors.
              if (((IReferencePropertyDescriptor) propertyDescriptor)
                  .getReverseRelationEnd() != null) {
                IPropertyDescriptor reversePropertyDescriptor = ((IReferencePropertyDescriptor) propertyDescriptor)
                    .getReverseRelationEnd();
                if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor) {
                  // force initialization
                  getAccessorFactory(context).createPropertyAccessor(
                      property.getKey(), entity.getContract()).getValue(entity);
                  reversePropertyDescriptor.preprocessSetter(property
                      .getValue(), null);
                } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
                  Collection<?> reverseCollection = (Collection<?>) getAccessorFactory(
                      context).createPropertyAccessor(
                      reversePropertyDescriptor.getName(),
                      ((IComponent) property.getValue()).getContract())
                      .getValue(property.getValue());
                  ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor)
                      .preprocessRemover(property.getValue(),
                          reverseCollection, entity);
                }
              }
            } else {
              // set to null to clean reverse relation ends
              getAccessorFactory(context).createPropertyAccessor(
                  property.getKey(), entity.getContract()).setValue(entity,
                  null);
              // but technically reset to original value to avoid Hibernate
              // not-null checks
              entity
                  .straightSetProperty(property.getKey(), property.getValue());
            }
          } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
            if (((ICollectionPropertyDescriptor) propertyDescriptor)
                .isComposition()) {
              // force Initialization of collection property.
              getAccessorFactory(context).createPropertyAccessor(
                  property.getKey(), entity.getContract()).getValue(entity);
              for (IEntity composedEntity : new ArrayList<IEntity>(
                  (Collection<IEntity>) property.getValue())) {
                cleanRelationshipsOnDeletion(composedEntity, context, dryRun);
              }
            } else if (propertyDescriptor.isModifiable()) {
              if (dryRun && property.getValue() != null) {
                // manually trigger reverse relations preprocessors.
                if (((ICollectionPropertyDescriptor) propertyDescriptor)
                    .getReverseRelationEnd() != null) {
                  IPropertyDescriptor reversePropertyDescriptor = ((ICollectionPropertyDescriptor<?>) propertyDescriptor)
                      .getReverseRelationEnd();
                  if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor) {
                    reversePropertyDescriptor.preprocessSetter(property
                        .getValue(), null);
                  } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
                    // initialize collection
                    getAccessorFactory(context).createPropertyAccessor(
                        property.getKey(), entity.getContract()).getValue(
                        entity);
                    for (Object collectionElement : (Collection<?>) property
                        .getValue()) {
                      Collection<?> reverseCollection = (Collection<?>) getAccessorFactory(
                          context).createPropertyAccessor(
                          reversePropertyDescriptor.getName(),
                          ((IComponent) collectionElement).getContract())
                          .getValue(collectionElement);
                      ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor)
                          .preprocessRemover(collectionElement,
                              reverseCollection, entity);
                    }
                  }
                }
              } else {
                getAccessorFactory(context).createPropertyAccessor(
                    property.getKey(), entity.getContract()).setValue(entity,
                    null);
              }
            }
          }
        }
      }
    } finally {
      entity.setPropertyProcessorsEnabled(true);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected HibernateBackendController getController(Map<String, Object> context) {
    return (HibernateBackendController) super.getController(context);
  }

  /**
   * Gets the hibernateTemplate.
   * 
   * @param context
   *          the action context.
   * @return the hibernateTemplate.
   */
  protected HibernateTemplate getHibernateTemplate(Map<String, Object> context) {
    return getController(context).getHibernateTemplate();
  }

  /**
   * Gets the transactionTemplate.
   * 
   * @param context
   *          the action context.
   * @return the transactionTemplate.
   */
  protected TransactionTemplate getTransactionTemplate(
      Map<String, Object> context) {
    return getController(context).getTransactionTemplate();
  }

  /**
   * Reloads an entity in hibernate.
   * 
   * @param entity
   *          the entity to reload.
   * @param context
   *          the action context.
   */
  protected void reloadEntity(IEntity entity, Map<String, Object> context) {
    if (entity.isPersistent()) {
      HibernateTemplate hibernateTemplate = getHibernateTemplate(context);
      getApplicationSession(context).merge(
          (IEntity) hibernateTemplate.load(entity.getContract().getName(),
              entity.getId()), EMergeMode.MERGE_CLEAN_EAGER);
    }
  }
}
