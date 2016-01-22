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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.core.convert.converter.Converter;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorRegistry;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.util.bean.PropertyHelper;

/**
 * Custom converter for Jspresso entities.
 *
 * @author Vincent Vandenschrick
 */
public class JspressoEntityWriteConverter implements Converter<IEntity, DBObject> {

  private IComponentDescriptorRegistry  descriptorRegistry;
  private JspressoMappingMongoConverter converter;

  /**
   * Convert dB object.
   *
   * @param entity
   *     the entity
   * @return the dB object
   */
  @Override
  public DBObject convert(IEntity entity) {
    DBObject dbo = convertComponent(entity);
    dbo.put("_id", entity.getId());
    return dbo;
  }

  @SuppressWarnings("unchecked")
  private DBObject convertComponent(IComponent component) {
    DBObject dbo = new BasicDBObject();
    Class<? extends IComponent> componentContract = component.getComponentContract();
    IComponentDescriptor<? extends IEntity> entityDescriptor = (IComponentDescriptor<? extends IEntity>)
        getDescriptorRegistry()
        .getComponentDescriptor(componentContract);
    for (Map.Entry<String, Object> propertyEntry : component.straightGetProperties().entrySet()) {
      Object propertyValue = propertyEntry.getValue();
      String propertyName = propertyEntry.getKey();
      IPropertyDescriptor propertyDescriptor = entityDescriptor.getPropertyDescriptor(propertyName);
      if (propertyDescriptor != null && !propertyDescriptor.isComputed() && !IEntity.ID.equals(propertyName)) {
        String convertedPropertyName = getConverter().getMappingContext().getPersistentEntity(componentContract)
                                                     .getPersistentProperty(PropertyHelper.toJavaBeanPropertyName(propertyName))
                                                     .getFieldName();
        if (propertyValue instanceof IComponent) {
          if (propertyValue instanceof IEntity) {
            dbo.put(convertedPropertyName, ((IEntity) propertyValue).getId());
          } else {
            dbo.put(convertedPropertyName, convertComponent((IComponent) propertyValue));
          }
        } else if (propertyValue instanceof Collection<?>) {
          Collection<Object> convertedCollection;
          if (propertyValue instanceof List) {
            convertedCollection = new ArrayList<>();
          } else {
            convertedCollection = new HashSet<>();
          }
          for (Object element : (Collection) propertyValue) {
            if (element instanceof IComponent) {
              if (element instanceof IEntity) {
                convertedCollection.add(((IEntity) element).getId());
              } else {
                convertedCollection.add(convertComponent((IComponent) element));
              }
            } else {
              convertedCollection.add(element);
            }
          }
          dbo.put(convertedPropertyName, convertedCollection);
        } else if (propertyValue != null) {
          // Do not store null values
          Object convertedPropertyValue = getConverter().convertToMongoType(propertyValue);
          dbo.put(convertedPropertyName, convertedPropertyValue);
        }
      }
    }
    return dbo;
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

  /**
   * Gets converter.
   *
   * @return the converter
   */
  protected JspressoMappingMongoConverter getConverter() {
    return converter;
  }

  /**
   * Sets converter.
   *
   * @param converter
   *     the converter
   */
  public void setConverter(JspressoMappingMongoConverter converter) {
    this.converter = converter;
  }
}
