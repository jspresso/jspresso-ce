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
package org.jspresso.framework.model.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptorAware;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.accessor.ICollectionAccessor;

/**
 * Performs a copy of the entity. It may be used in application actions to
 * smartly duplicate an entity.
 *
 * @author Vincent Vandenschrick
 */
public class SmartEntityCloneFactory extends CarbonEntityCloneFactory {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <E extends IComponent> E cloneComponent(E componentToClone, IEntityFactory entityFactory) {
    E clonedComponent = (E) entityFactory.createComponentInstance(getComponentContract(componentToClone));
    handleRelationships(componentToClone, clonedComponent, entityFactory);
    return clonedComponent;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <E extends IEntity> E cloneEntity(E entityToClone, IEntityFactory entityFactory) {
    E clonedEntity = (E) entityFactory.createEntityInstance(getComponentContract(entityToClone));
    handleRelationships(entityToClone, clonedEntity, entityFactory);
    return clonedEntity;
  }

  /**
   * Sets the accessorFactory.
   *
   * @param accessorFactory
   *     the accessorFactory to set.
   * @deprecated accessor factory is now retrieved from the entity factory
   * passed as parameter of the methods.
   */
  @SuppressWarnings({"EmptyMethod", "UnusedParameters"})
  @Deprecated
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    // NO-OP
  }

  /**
   * Whether the object is fully initialized.
   *
   * @param objectOrProxy
   *     the object to test.
   * @return true if the object is fully initialized.
   */
  protected boolean isInitialized(Object objectOrProxy) {
    return true;
  }

  private <E extends IComponent> void handleRelationships(E componentToClone, E clonedComponent,
                                                          IEntityFactory entityFactory) {
    IComponentDescriptor<?> componentDescriptor = entityFactory.getComponentDescriptor(
        getComponentContract(componentToClone));

    Map<Object, ICollectionPropertyDescriptor<?>> collRelToUpdate = new HashMap<>();
    for (Map.Entry<String, Object> propertyEntry : componentToClone.straightGetProperties().entrySet()) {
      String propertyName = propertyEntry.getKey();
      Object propertyValue = propertyEntry.getValue();
      if (propertyValue != null && !(IEntity.ID.equals(propertyName) || IEntity.VERSION.equals(propertyName)
          || componentDescriptor.getUnclonedProperties().contains(propertyName))) {
        IPropertyDescriptor propertyDescriptor = componentDescriptor.getPropertyDescriptor(propertyName);
        if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
          if (propertyValue instanceof IComponent && !(propertyValue instanceof IEntity)) {
            clonedComponent.straightSetProperty(propertyName,
                cloneComponent((IComponent) propertyValue, entityFactory));
          } else {
            IRelationshipEndPropertyDescriptor reverseDescriptor = ((IRelationshipEndPropertyDescriptor)
                propertyDescriptor)
                .getReverseRelationEnd();
            if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
              if (!(reverseDescriptor instanceof IReferencePropertyDescriptor<?>)) {
                if (((IRelationshipEndPropertyDescriptor) propertyDescriptor).isComposition()) {
                  if (!isInitialized(propertyValue)) {
                    try {
                      // Forces initialization
                      entityFactory.getAccessorFactory().createPropertyAccessor(propertyName,
                          componentDescriptor.getComponentContract()).getValue(componentToClone);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                      // Ignore
                    }
                  }
                  clonedComponent.straightSetProperty(propertyName,
                      cloneEntity((IEntity) propertyValue, entityFactory));
                } else {
                  clonedComponent.straightSetProperty(propertyName, propertyValue);
                  if (reverseDescriptor instanceof ICollectionPropertyDescriptor<?>) {
                    if (isInitialized(propertyValue)) {
                      collRelToUpdate.put(propertyValue, (ICollectionPropertyDescriptor<?>) reverseDescriptor);
                    }
                  }
                }
              }
            } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor<?>) {
              if (reverseDescriptor instanceof ICollectionPropertyDescriptor<?>) {
                // We must force initialization of the collection. So do a get.
                try {
                  entityFactory.getAccessorFactory().createPropertyAccessor(propertyName,
                      getComponentContract(componentToClone)).getValue(componentToClone);
                } catch (IllegalAccessException | NoSuchMethodException ex) {
                  throw new EntityException(ex);
                } catch (InvocationTargetException ex) {
                  if (ex.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) ex.getCause();
                  }
                  throw new EntityException(ex.getCause());
                }
                for (Object reverseCollectionElement : (Collection<?>) propertyValue) {
                  if (isInitialized(reverseCollectionElement)) {
                    collRelToUpdate.put(reverseCollectionElement, (ICollectionPropertyDescriptor<?>) reverseDescriptor);
                  }
                }
              }
            }
          }
        } else {
          clonedComponent.straightSetProperty(propertyName, ObjectUtils.cloneIfPossible(propertyValue));
        }
      }
    }
    for (Map.Entry<Object, ICollectionPropertyDescriptor<?>> collectionEntry : collRelToUpdate.entrySet()) {
      ICollectionPropertyDescriptor<?> collectionDescriptor = collectionEntry.getValue();
      Class<?> masterContract = null;
      if (collectionDescriptor.getReverseRelationEnd() instanceof IReferencePropertyDescriptor<?>) {
        masterContract = ((IReferencePropertyDescriptor<?>) collectionDescriptor.getReverseRelationEnd())
            .getReferencedDescriptor().getComponentContract();
      } else if (collectionDescriptor.getReverseRelationEnd() instanceof ICollectionPropertyDescriptor<?>) {
        masterContract = ((ICollectionPropertyDescriptor<?>) collectionDescriptor.getReverseRelationEnd())
            .getReferencedDescriptor().getElementDescriptor().getComponentContract();
      }
      ICollectionAccessor collectionAccessor = entityFactory.getAccessorFactory().createCollectionPropertyAccessor(
          collectionDescriptor.getName(), masterContract, getComponentContract(clonedComponent));
      if (collectionAccessor instanceof IModelDescriptorAware) {
        ((IModelDescriptorAware) collectionAccessor).setModelDescriptor(collectionDescriptor);
      }
      try {
        Collection<?> existingCollection = collectionAccessor.getValue(collectionEntry.getKey());
        if (!existingCollection.contains(clonedComponent)) {
          // it could already be there through lifecycle handlers / property
          // processors.
          collectionAccessor.addToValue(collectionEntry.getKey(), clonedComponent);
        }
      } catch (IllegalAccessException | NoSuchMethodException ex) {
        throw new EntityException(ex);
      } catch (InvocationTargetException ex) {
        if (ex.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex.getCause();
        }
        throw new EntityException(ex.getCause());
      }
    }
  }

  /**
   * Gets component contract.
   *
   * @param component
   *     the component to clone
   * @param <E>
   *     the type parameter
   * @return the component contract
   */
  protected <E extends IComponent> Class<? extends E> getComponentContract(E component) {
    return (Class<E>) component.getComponentContract();
  }
}
