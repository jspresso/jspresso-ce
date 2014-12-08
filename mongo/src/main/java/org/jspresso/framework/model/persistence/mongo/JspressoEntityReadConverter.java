/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.persistence.mongo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * Custom converter for Jspresso entities.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class JspressoEntityReadConverter implements ConditionalGenericConverter {

  private IEntityFactory                entityFactory;
  private IComponentCollectionFactory   collectionFactory;
  private JspressoMappingMongoConverter mongoConverter;

  /**
   * Convert object.
   *
   * @param source
   *     the generic source
   * @param sourceType
   *     the source type
   * @param targetType
   *     the target type
   * @return the object
   */
  @Override
  public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
    return convertEntity((DBObject) source);
  }

  private Object convertEntity(DBObject source) {
    Serializable id = (Serializable) source.get("_id");
    Class<? extends IEntity> entityType;
    try {
      entityType = (Class<? extends IEntity>) Class.forName((String) source.get("_class"));
    } catch (ClassNotFoundException e) {
      throw new NestedRuntimeException(e);
    }
    IComponentDescriptor<? extends IEntity> entityDescriptor = (IComponentDescriptor<? extends IEntity>)
        getEntityFactory()
        .getComponentDescriptor(entityType);
    IEntity entity = getEntityFactory().createEntityInstance(entityType, id);
    completeComponent(source, entityDescriptor, entity);
    return entity;
  }

  private Object convertComponent(DBObject source) {
    Class<? extends IComponent> componentType;
    try {
      componentType = (Class<? extends IComponent>) Class.forName((String) source.get("_class"));
    } catch (ClassNotFoundException e) {
      throw new NestedRuntimeException(e);
    }
    IComponentDescriptor<? extends IComponent> componentDescriptor = (IComponentDescriptor<? extends IComponent>)
        getEntityFactory()
        .getComponentDescriptor(componentType);
    IComponent component = getEntityFactory().createComponentInstance(componentType);
    completeComponent(source, componentDescriptor, component);
    return component;
  }

  private void completeComponent(DBObject source, IComponentDescriptor<? extends IComponent> entityDescriptor,
                                 IComponent component) {
    for (IPropertyDescriptor propertyDescriptor : entityDescriptor.getPropertyDescriptors()) {
      if (propertyDescriptor != null && !propertyDescriptor.isComputed()) {
        String propertyName = propertyDescriptor.getName();
        if (source.containsField(propertyName)) {
          Object propertyValue = source.get(propertyName);
          if (propertyValue instanceof DBObject) {
            if (propertyValue instanceof BasicDBList) {
              Collection<Object> collectionProperty = getCollectionFactory().createComponentCollection(
                  ((ICollectionPropertyDescriptor) propertyDescriptor).getCollectionDescriptor()
                                                                      .getCollectionInterface());
              for (Object element : (BasicDBList) propertyValue) {
                if (element instanceof DBObject) {
                  collectionProperty.add(convertComponent((DBObject) element));
                } else if (element instanceof DBRef) {
                  collectionProperty.add(convertEntity(((DBRef) element).fetch()));
                } else {
                  collectionProperty.add(element);
                }
              }
              component.straightSetProperty(propertyName, collectionProperty);
            } else {
              component.straightSetProperty(propertyName, convertComponent((DBObject) propertyValue));
            }
          } else if (propertyValue instanceof DBRef) {
            //TODO Lazy loading
            component.straightSetProperty(propertyName, convertEntity(((DBRef) propertyValue).fetch()));
          } else {
            component.straightSetProperty(propertyName, propertyValue);
          }
        }
      }
    }
  }


  /**
   * Gets entity factory.
   *
   * @return the entity factory
   */
  protected IEntityFactory getEntityFactory() {
    return entityFactory;
  }

  /**
   * Sets entity factory.
   *
   * @param entityFactory
   *     the entity factory
   */
  public void setEntityFactory(IEntityFactory entityFactory) {
    this.entityFactory = entityFactory;
  }

  /**
   * Matches boolean.
   *
   * @param sourceType
   *     the source type
   * @param targetType
   *     the target type
   * @return the boolean
   */
  @Override
  public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
    return DBObject.class.isAssignableFrom(sourceType.getType()) && IEntity.class.isAssignableFrom(
        targetType.getType());
  }

  /**
   * Gets convertible types.
   *
   * @return the convertible types
   */
  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return Collections.singleton(new ConvertiblePair(DBObject.class, IEntity.class));
  }

  /**
   * Gets mongo converter.
   *
   * @return the mongo converter
   */
  protected JspressoMappingMongoConverter getMongoConverter() {
    return mongoConverter;
  }

  /**
   * Sets mongo converter.
   *
   * @param mongoConverter
   *     the mongo converter
   */
  public void setMongoConverter(JspressoMappingMongoConverter mongoConverter) {
    this.mongoConverter = mongoConverter;
  }

  /**
   * Gets collection factory.
   *
   * @return the collection factory
   */
  protected IComponentCollectionFactory getCollectionFactory() {
    return collectionFactory;
  }

  /**
   * Sets collection factory.
   *
   * @param collectionFactory
   *     the collection factory
   */
  public void setCollectionFactory(IComponentCollectionFactory collectionFactory) {
    this.collectionFactory = collectionFactory;
  }
}
