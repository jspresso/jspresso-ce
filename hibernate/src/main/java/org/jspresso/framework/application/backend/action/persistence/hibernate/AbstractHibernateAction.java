/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.proxy.HibernateProxy;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * This the root abstract class of all hibernate related persistence actions.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractHibernateAction extends BackendAction {

  /**
   * Performs necessary cleanings when an entity or component is deleted.
   * 
   * @param component
   *          the deleted entity or component.
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
  protected void cleanRelationshipsOnDeletion(IComponent component,
      Map<String, Object> context, boolean dryRun)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    cleanRelationshipsOnDeletion(component, context, dryRun,
        new HashSet<IComponent>());
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
      getController(context).merge(
          (IEntity) hibernateTemplate.load(entity.getComponentContract()
              .getName(), entity.getId()), EMergeMode.MERGE_CLEAN_EAGER);
    }
  }

  @SuppressWarnings("unchecked")
  private void cleanRelationshipsOnDeletion(IComponent componentOrProxy,
      Map<String, Object> context, boolean dryRun,
      Set<IComponent> clearedEntities) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    IComponent component;
    if (componentOrProxy instanceof HibernateProxy) {
      // we must unwrap the proxy to avoid class cast exceptions.
      // see
      // http://forum.hibernate.org/viewtopic.php?p=2323464&sid=cb4ba3a4418276e5d2fbdd6c906ba734
      component = (IComponent) ((HibernateProxy) componentOrProxy)
          .getHibernateLazyInitializer().getImplementation();
    } else {
      component = componentOrProxy;
    }
    if (clearedEntities.contains(component)) {
      return;
    }
    clearedEntities.add(component);
    try {
      component.setPropertyProcessorsEnabled(false);
      IComponentDescriptor<?> componentDescriptor = getEntityFactory(context)
          .getComponentDescriptor(component.getComponentContract());
      for (Map.Entry<String, Object> property : component
          .straightGetProperties().entrySet()) {
        if (property.getValue() != null) {
          IPropertyDescriptor propertyDescriptor = componentDescriptor
              .getPropertyDescriptor(property.getKey());
          if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
            // force initialization of relationship property.
            getAccessorFactory(context).createPropertyAccessor(
                property.getKey(), component.getComponentContract()).getValue(
                component);
            if (propertyDescriptor instanceof IReferencePropertyDescriptor
                && property.getValue() instanceof IEntity) {
              if (((IRelationshipEndPropertyDescriptor) propertyDescriptor)
                  .isComposition()) {
                cleanRelationshipsOnDeletion((IEntity) property.getValue(),
                    context, dryRun, clearedEntities);
              } else {
                if (dryRun) {
                  // manually trigger reverse relations preprocessors.
                  if (((IRelationshipEndPropertyDescriptor) propertyDescriptor)
                      .getReverseRelationEnd() != null
                      && !((IRelationshipEndPropertyDescriptor) propertyDescriptor)
                          .isComposition()) {
                    IPropertyDescriptor reversePropertyDescriptor = ((IReferencePropertyDescriptor<?>) propertyDescriptor)
                        .getReverseRelationEnd();
                    if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor) {
                      reversePropertyDescriptor.preprocessSetter(property
                          .getValue(), null);
                    } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
                      Collection<?> reverseCollection = (Collection<?>) getAccessorFactory(
                          context).createPropertyAccessor(
                          reversePropertyDescriptor.getName(),
                          ((IComponent) property.getValue())
                              .getComponentContract()).getValue(
                          property.getValue());
                      ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor)
                          .preprocessRemover(property.getValue(),
                              reverseCollection, component);
                    }
                  }
                } else {
                  // test to see if we already traversed the reverse
                  // relationship that is a composition.
                  if (((IRelationshipEndPropertyDescriptor) propertyDescriptor)
                      .getReverseRelationEnd() == null
                      || !(((IRelationshipEndPropertyDescriptor) propertyDescriptor)
                          .getReverseRelationEnd().isComposition() && clearedEntities
                          .contains(property.getValue()))) {
                    // set to null to clean reverse relation ends
                    getAccessorFactory(context).createPropertyAccessor(
                        property.getKey(), component.getComponentContract())
                        .setValue(component, null);
                    // but technically reset to original value to avoid
                    // Hibernate
                    // not-null checks
                    component.straightSetProperty(property.getKey(), property
                        .getValue());
                  }
                }
              }
            } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
              if (((ICollectionPropertyDescriptor<?>) propertyDescriptor)
                  .isComposition()) {
                for (IComponent composedEntity : new ArrayList<IComponent>(
                    (Collection<IComponent>) property.getValue())) {
                  cleanRelationshipsOnDeletion(composedEntity, context, dryRun,
                      clearedEntities);
                }
              } else if (propertyDescriptor.isModifiable()) {
                if (dryRun) {
                  // manually trigger reverse relations preprocessors.
                  if (((ICollectionPropertyDescriptor<?>) propertyDescriptor)
                      .getReverseRelationEnd() != null) {
                    IPropertyDescriptor reversePropertyDescriptor = ((ICollectionPropertyDescriptor<?>) propertyDescriptor)
                        .getReverseRelationEnd();
                    if (reversePropertyDescriptor instanceof IReferencePropertyDescriptor) {
                      reversePropertyDescriptor.preprocessSetter(property
                          .getValue(), null);
                    } else if (reversePropertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
                      for (Object collectionElement : (Collection<?>) property
                          .getValue()) {
                        Collection<?> reverseCollection = (Collection<?>) getAccessorFactory(
                            context).createPropertyAccessor(
                            reversePropertyDescriptor.getName(),
                            ((IComponent) collectionElement)
                                .getComponentContract()).getValue(
                            collectionElement);
                        ((ICollectionPropertyDescriptor<?>) reversePropertyDescriptor)
                            .preprocessRemover(collectionElement,
                                reverseCollection, component);
                      }
                    }
                  }
                } else {
                  getAccessorFactory(context).createPropertyAccessor(
                      property.getKey(), component.getComponentContract())
                      .setValue(component, null);
                }
              }
            }
          }
        }
      }
    } finally {
      component.setPropertyProcessorsEnabled(true);
    }
  }
}
