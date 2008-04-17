/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SmartEntityCloneFactory extends CarbonEntityCloneFactory {

  private IAccessorFactory accessorFactory;

  /**
   * {@inheritDoc}
   */
  @Override
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
          if (propertyEntry.getValue() instanceof IComponent
              && !(propertyEntry.getValue() instanceof IEntity)) {
            clonedEntity.straightSetProperty(propertyEntry.getKey(),
                cloneComponent((IComponent) propertyEntry.getValue(),
                    entityFactory));
          } else {
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
      if (collectionAccessor instanceof IModelDescriptorAware) {
        ((IModelDescriptorAware) collectionAccessor)
            .setModelDescriptor(collectionDescriptor);
      }
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
   * Sets the accessorFactory.
   * 
   * @param accessorFactory
   *            the accessorFactory to set.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * Wether the object is fully initialized.
   * 
   * @param objectOrProxy
   *            the object to test.
   * @return true if the object is fully initialized.
   */
  protected boolean isInitialized(@SuppressWarnings("unused")
  Object objectOrProxy) {
    return true;
  }

}
