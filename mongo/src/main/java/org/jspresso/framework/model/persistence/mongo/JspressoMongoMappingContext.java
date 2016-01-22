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
package org.jspresso.framework.model.persistence.mongo;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.mapping.SimplePropertyHandler;
import org.springframework.data.mapping.context.PersistentPropertyPath;
import org.springframework.data.mapping.model.AbstractPersistentProperty;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.util.TypeInformation;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorRegistry;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.bean.PropertyHelper;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.reflect.ReflectHelper;

/**
 * Custom Jspresso Mongo mapping context.
 *
 * @author Vincent Vandenschrick
 */
public class JspressoMongoMappingContext extends MongoMappingContext {

  private static final Logger LOG = LoggerFactory.getLogger(JspressoMongoMappingContext.class);

  private IComponentDescriptorRegistry descriptorRegistry;

  /**
   * Gets persistent entity.
   *
   * @param type
   *     the type
   * @return the persistent entity
   */
  @Override
  public BasicMongoPersistentEntity<?> getPersistentEntity(Class<?> type) {
    return super.getPersistentEntity(getEntityContractFromType(type));
  }

  /**
   * Has persistent entity for.
   *
   * @param type
   *     the type
   * @return the boolean
   */
  @Override
  public boolean hasPersistentEntityFor(Class<?> type) {
    return super.hasPersistentEntityFor(getEntityContractFromType(type));
  }

  /**
   * Gets persistent property path.
   *
   * @param propertyPath
   *     the property path
   * @param type
   *     the type
   * @return the persistent property path
   */
  @Override
  public PersistentPropertyPath<MongoPersistentProperty> getPersistentPropertyPath(String propertyPath, Class<?> type) {
    StringBuilder fixedPropertyPath = new StringBuilder();
    Class<?> entityContract = getEntityContractFromType(type);
    IComponentDescriptor<?> baseComponentDescriptor = descriptorRegistry.getComponentDescriptor(entityContract);
    for (String propertyName : propertyPath.split("\\.")) {
      IPropertyDescriptor propertyDescriptor = baseComponentDescriptor.getPropertyDescriptor(propertyName);
      String fixedPropertyName = propertyName;
      if (propertyDescriptor == null) {
        for (IPropertyDescriptor pd : baseComponentDescriptor.getPropertyDescriptors()) {
          if (propertyName.equals(pd.getPersistenceFormula())) {
            fixedPropertyName = pd.getName();
          }
        }
      }
      if (fixedPropertyPath.length() > 0) {
        fixedPropertyPath.append(".");
      }
      fixedPropertyPath.append(fixedPropertyName);
    }
    return super.getPersistentPropertyPath(fixedPropertyPath.toString(), entityContract);
  }

  /**
   * Gets persistent property path.
   *
   * @param propertyPath
   *     the property path
   * @return the persistent property path
   */
  @Override
  public PersistentPropertyPath<MongoPersistentProperty> getPersistentPropertyPath(PropertyPath propertyPath) {
    return getPersistentPropertyPath(propertyPath.toDotPath(), propertyPath.getOwningType().getType());
  }

  /**
   * Add persistent entity.
   *
   * @param type
   *     the type
   * @return the basic mongo persistent entity
   */
  @Override
  protected BasicMongoPersistentEntity<?> addPersistentEntity(Class<?> type) {
    return super.addPersistentEntity(getEntityContractFromType(type));
  }

  /**
   * Add persistent entity.
   *
   * @param typeInformation
   *     the type information
   * @return the basic mongo persistent entity
   */
  @SuppressWarnings("unchecked")
  @Override
  protected BasicMongoPersistentEntity<?> addPersistentEntity(TypeInformation<?> typeInformation) {
    final BasicMongoPersistentEntity<?> entity = super.addPersistentEntity(typeInformation);
    final Class<?> entityType = typeInformation.getType();
    final IComponentDescriptor<? extends IEntity> entityDescriptor = (IComponentDescriptor<? extends IEntity>)
        getDescriptorRegistry()
        .getComponentDescriptor(entityType);
    if (entityDescriptor != null) {
      final Set<String> entityDeclaredPropertyNames = new HashSet<>();
      try {
        for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(entityType).getPropertyDescriptors()) {
          entityDeclaredPropertyNames.add(propertyDescriptor.getName());
        }
      } catch (IntrospectionException e) {
        LOG.error("Could not extract bean info from class {}", entityType, e);
        throw new NestedRuntimeException(e);
      }
      for (Class<?> superInterface : entityType.getInterfaces()) {
        final IComponentDescriptor<? extends IEntity> parentDescriptor = (IComponentDescriptor<? extends IEntity>)
            getDescriptorRegistry()
            .getComponentDescriptor(superInterface);
        if (parentDescriptor != null) {
          BasicMongoPersistentEntity<?> superEntity = getPersistentEntity(superInterface);
          if (superEntity != null) {
            superEntity.doWithProperties(new SimplePropertyHandler() {
              @Override
              public void doWithPersistentProperty(PersistentProperty<?> parentPersistentProperty) {
                String propertyName = parentPersistentProperty.getName();
                IPropertyDescriptor propertyDescriptor = parentDescriptor.getPropertyDescriptor(propertyName);
                MongoPersistentProperty declaredPersistentProperty = entity.getPersistentProperty(
                    PropertyHelper.toJavaBeanPropertyName(propertyName));
                if (declaredPersistentProperty != null && declaredPersistentProperty.getSetter() == null &&
                    parentPersistentProperty.getSetter() != null) {
                  try {
                    // Some properties will be writable in parent but not locally, so we must fix their descriptor.
                    PropertyDescriptor localDescriptor = (PropertyDescriptor) ReflectHelper.getPrivateFieldValue(
                        AbstractPersistentProperty.class, "propertyDescriptor", declaredPersistentProperty);
                    localDescriptor.setWriteMethod(parentPersistentProperty.getSetter());
                  } catch (IllegalAccessException | NoSuchFieldException | IntrospectionException e) {
                    LOG.error("Could not extract propertyDescriptor {} from persistent class {}", propertyName,
                        entityType.getName());
                  }
                }
                if (parentPersistentProperty instanceof MongoPersistentProperty && declaredPersistentProperty == null
                    && !entityDeclaredPropertyNames.contains(propertyName) && propertyDescriptor != null
                    && !propertyDescriptor.isComputed()) {
                  entity.addPersistentProperty((MongoPersistentProperty) parentPersistentProperty);
                }
              }
            });
          }
        }
      }
    }
    return entity;
  }

  private Class<?> getEntityContractFromType(Class<?> type) {
    if (Proxy.isProxyClass(type)) {
      for (Class<?> superInterface : type.getInterfaces()) {
        if (IEntity.class.isAssignableFrom(superInterface)) {
          return superInterface;
        }
      }
    }
    return type;
  }

  /**
   * Gets descriptor registry.
   *
   * @return the descriptor registry
   */
  protected IComponentDescriptorRegistry getDescriptorRegistry() {
    return descriptorRegistry;
  }

  /**
   * Sets descriptor registry.
   *
   * @param descriptorRegistry
   *     the descriptor registry
   */
  public void setDescriptorRegistry(IComponentDescriptorRegistry descriptorRegistry) {
    this.descriptorRegistry = descriptorRegistry;
  }
}
