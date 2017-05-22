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
package org.jspresso.framework.model.descriptor.basic;

import java.io.Serializable;

import org.jspresso.framework.model.descriptor.DescriptorException;
import org.jspresso.framework.model.descriptor.IJavaSerializablePropertyDescriptor;

/**
 * Describes a property used to store any java {@code Serializable} object.
 * The property value is serialized/de-serialized to/from the data store. The
 * operation is completely transparent to the developer, i.e. the developer
 * never plays with the serialized form.
 *
 * @author Vincent Vandenschrick
 */
public class BasicJavaSerializablePropertyDescriptor extends BasicBinaryPropertyDescriptor
    implements IJavaSerializablePropertyDescriptor {

  private String modelTypeClassName;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicJavaSerializablePropertyDescriptor clone() {
    BasicJavaSerializablePropertyDescriptor clonedDescriptor = (BasicJavaSerializablePropertyDescriptor) super.clone();

    return clonedDescriptor;
  }

  /**
   * Returns Object class or the class refined by
   * {@code modelTypeClassName}.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Class<?> getModelType() {
    if (modelTypeClassName != null) {
      String refinedClassName = modelTypeClassName;
      if (modelTypeClassName.contains("<")) {
        refinedClassName = modelTypeClassName.substring(0, modelTypeClassName.indexOf("<"));
      }
      try {
        return Class.forName(refinedClassName);
      } catch (Exception ex) {
        throw new DescriptorException(ex);
      }
    }
    return getDefaultModelType();
  }

  protected Class<?> getDefaultModelType() {
    return Serializable.class;
  }

  /**
   * Configures the actual property type through its fully qualified name.
   *
   * @param modelTypeClassName
   *     the modelTypeClassName to set.
   */
  public void setModelTypeClassName(String modelTypeClassName) {
    this.modelTypeClassName = modelTypeClassName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getModelTypeName() {
    if (this.modelTypeClassName != null) {
      return this.modelTypeClassName;
    }
    return getDefaultModelType().getName();
  }
}
