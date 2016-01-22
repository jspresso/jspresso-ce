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

import java.util.List;

import org.jspresso.framework.model.descriptor.DescriptorException;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IObjectPropertyDescriptor;

/**
 * This descriptor is used to describe an arbitrary object property for which
 * the type can be explicitly declared.
 *
 * @internal
 * @author Vincent Vandenschrick
 */
public class BasicObjectPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IObjectPropertyDescriptor {

  private String modelTypeClassName;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicObjectPropertyDescriptor clone() {
    BasicObjectPropertyDescriptor clonedDescriptor = (BasicObjectPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * Returns null.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IComponentDescriptor<Object> getComponentDescriptor() {
    return null;
  }

  /**
   * Returns null.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public List<String> getQueryableProperties() {
    return null;
  }

  /**
   * Returns null.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public List<String> getRenderedProperties() {
    return null;
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
    return Object.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isQueryable() {
    return false;
  }

  /**
   * Configures the actual property type through its fully qualified name.
   *
   * @param modelTypeClassName
   *          the modelTypeClassName to set.
   */
  public void setModelTypeClassName(String modelTypeClassName) {
    this.modelTypeClassName = modelTypeClassName;
  }

  /**
   * Returns {@code false}.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected boolean getDefaultSortablility() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getModelTypeName() {
    return this.modelTypeClassName;
  }
}
