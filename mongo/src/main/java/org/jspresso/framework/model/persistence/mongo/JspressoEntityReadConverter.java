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
import java.util.Collections;
import java.util.Set;

import com.mongodb.DBObject;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IScalarPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;

/**
 * Custom converter for Jspresso entities.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class JspressoEntityReadConverter implements ConditionalGenericConverter {

  private IEntityFactory entityFactory;

  @Override
  public Object convert(Object genericSource, TypeDescriptor sourceType, TypeDescriptor targetType) {
    DBObject source = (DBObject) genericSource;
    Serializable id = (Serializable) source.get("_id");
    Class<? extends IEntity> entityType = (Class<? extends IEntity>) targetType.getType();
    IComponentDescriptor<? extends IEntity> entityDescriptor = (IComponentDescriptor<? extends IEntity>)
        getEntityFactory().getComponentDescriptor(entityType);
    IEntity entity = getEntityFactory().createEntityInstance(entityType, id);
    for (IPropertyDescriptor propertyDescriptor : entityDescriptor.getPropertyDescriptors()) {
      if (propertyDescriptor != null && !propertyDescriptor.isComputed()) {
        String propertyName = propertyDescriptor.getName();
        if (source.containsField(propertyName)) {
          Object propertyValue = source.get(propertyName);
          if (propertyDescriptor instanceof IScalarPropertyDescriptor) {
            entity.straightSetProperty(propertyName, propertyValue);
          }
        }
      }
    }
    return entity;
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
   * @param entityFactory the entity factory
   */
  public void setEntityFactory(IEntityFactory entityFactory) {
    this.entityFactory = entityFactory;
  }

  /**
   * Matches boolean.
   *
   * @param sourceType the source type
   * @param targetType the target type
   * @return the boolean
   */
  @Override
  public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
    return DBObject.class.isAssignableFrom(sourceType.getType()) && IEntity.class.isAssignableFrom(targetType.getType());
  }

  @Override
  public Set<ConvertiblePair> getConvertibleTypes() {
    return Collections.singleton(new ConvertiblePair(DBObject.class, IEntity.class));
  }
}
