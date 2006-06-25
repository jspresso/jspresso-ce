/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import com.d2s.framework.util.accessor.IAccessorFactory;
import com.d2s.framework.util.accessor.ICollectionAccessor;

/**
 * Performs a copy of the entity. It may be used in application actions to
 * smartly duplicate an entity.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SmartEntityCloneFactory implements IEntityCloneFactory {

  private IAccessorFactory accessorFactory;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public <E extends IEntity> E cloneEntity(E entityToClone,
      IEntityFactory entityFactory) {
    E clonedEntity = (E) entityFactory.createEntityInstance(entityToClone
        .getContract());

    IComponentDescriptor componentDescriptor = entityFactory
        .getComponentDescriptor(entityToClone.getContract());

    Map<Object, ICollectionPropertyDescriptor> collRelToUpdate = new HashMap<Object, ICollectionPropertyDescriptor>();
    for (Map.Entry<String, Object> propertyEntry : entityToClone
        .straightGetProperties().entrySet()) {
      if (propertyEntry.getValue() != null
          && !(IEntity.ID.equals(propertyEntry.getKey())
              || IEntity.VERSION.equals(propertyEntry.getKey()) || componentDescriptor
              .getUnclonedProperties().contains(propertyEntry.getKey()))) {
        IPropertyDescriptor propertyDescriptor = componentDescriptor
            .getPropertyDescriptor(propertyEntry.getKey());
        if (propertyDescriptor instanceof IRelationshipEndPropertyDescriptor) {
          IRelationshipEndPropertyDescriptor reverseDescriptor = ((IRelationshipEndPropertyDescriptor) propertyDescriptor)
              .getReverseRelationEnd();
          if (propertyDescriptor instanceof IReferencePropertyDescriptor) {
            if (!(reverseDescriptor instanceof IReferencePropertyDescriptor)) {
              clonedEntity.straightSetProperty(propertyEntry.getKey(),
                  propertyEntry.getValue());
              if (reverseDescriptor instanceof ICollectionPropertyDescriptor) {
                if (isInitialized(propertyEntry.getValue())) {
                  collRelToUpdate.put(propertyEntry.getValue(),
                      (ICollectionPropertyDescriptor) reverseDescriptor);
                }
              }
            }
          } else if (propertyDescriptor instanceof ICollectionPropertyDescriptor) {
            if (reverseDescriptor instanceof ICollectionPropertyDescriptor) {
              for (Object reverseCollectionElement : (Collection) propertyEntry
                  .getValue()) {
                if (isInitialized(reverseCollectionElement)) {
                  collRelToUpdate.put(reverseCollectionElement,
                      (ICollectionPropertyDescriptor) reverseDescriptor);
                }
              }
            }
          }
        } else {
          clonedEntity.straightSetProperty(propertyEntry.getKey(),
              propertyEntry.getValue());
        }
      }
    }
    for (Map.Entry<Object, ICollectionPropertyDescriptor> collectionEntry : collRelToUpdate
        .entrySet()) {
      ICollectionPropertyDescriptor collectionDescriptor = collectionEntry
          .getValue();
      Class masterContract = null;
      if (collectionDescriptor.getReverseRelationEnd() instanceof IReferencePropertyDescriptor) {
        masterContract = ((IReferencePropertyDescriptor) collectionDescriptor
            .getReverseRelationEnd()).getReferencedDescriptor()
            .getComponentContract();
      } else if (collectionDescriptor.getReverseRelationEnd() instanceof ICollectionPropertyDescriptor) {
        masterContract = ((ICollectionPropertyDescriptor) collectionDescriptor
            .getReverseRelationEnd()).getReferencedDescriptor()
            .getElementDescriptor().getComponentContract();
      }
      ICollectionAccessor collectionAccessor = accessorFactory
          .createCollectionPropertyAccessor(collectionDescriptor.getName(),
              masterContract, clonedEntity.getContract());
      try {
        collectionAccessor.addToValue(collectionEntry.getKey(), clonedEntity);
      } catch (IllegalAccessException ex) {
        throw new EntityException(ex);
      } catch (InvocationTargetException ex) {
        throw new EntityException(ex);
      } catch (NoSuchMethodException ex) {
        throw new EntityException(ex);
      }
    }
    return clonedEntity;
  }

  /**
   * Wether the object is fully initialized.
   * 
   * @param objectOrProxy
   *          the object to test.
   * @return true if the object is fully initialized.
   */
  protected boolean isInitialized(@SuppressWarnings("unused")
  Object objectOrProxy) {
    return true;
  }

  /**
   * Sets the accessorFactory.
   * 
   * @param accessorFactory
   *          the accessorFactory to set.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

}
