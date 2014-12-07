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

import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.core.convert.converter.Converter;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorRegistry;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IScalarPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;

/**
 * Custom converter for Jspresso entities.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class JspressoEntityWriteConverter implements Converter<IEntity, DBObject> {

  private IComponentDescriptorRegistry descriptorRegistry;

  /**
   * Convert dB object.
   *
   * @param source the source
   * @return the dB object
   */
  @Override
  public DBObject convert(IEntity source) {
    DBObject dbo = new BasicDBObject();
    dbo.put("_id", source.getId());
    dbo.put("_class", source.getComponentContract().getName());
    IComponentDescriptor<? extends IEntity> entityDescriptor
        = (IComponentDescriptor<? extends IEntity>) getDescriptorRegistry().getComponentDescriptor(source.getComponentContract());
    for (Map.Entry<String, Object> propertyEntry : source.straightGetProperties().entrySet()) {
      Object propertyValue = propertyEntry.getValue();
      String propertyName = propertyEntry.getKey();
      IPropertyDescriptor propertyDescriptor = entityDescriptor.getPropertyDescriptor(propertyName);
      if (propertyDescriptor != null && !propertyDescriptor.isComputed()) {
        if (propertyDescriptor instanceof IScalarPropertyDescriptor) {
          dbo.put(propertyName, propertyValue);
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
   * @param descriptorRegistry the descriptor registry
   */
  public void setDescriptorRegistry(IComponentDescriptorRegistry descriptorRegistry) {
    this.descriptorRegistry = descriptorRegistry;
  }
}
