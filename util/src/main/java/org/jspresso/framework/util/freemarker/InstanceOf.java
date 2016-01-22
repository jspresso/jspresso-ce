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
package org.jspresso.framework.util.freemarker;

import java.util.List;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BooleanModel;
import freemarker.template.AdapterTemplateModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 * Tells whether an object is instance of a class.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("rawtypes")
public class InstanceOf implements TemplateMethodModelEx {

  private final BeansWrapper wrapper;

  /**
   * Constructs a new {@code InstanceOf} instance.
   *
   * @param wrapper
   *          the beans wrapper.
   */
  public InstanceOf(BeansWrapper wrapper) {
    this.wrapper = wrapper;
  }

  /**
   * Tells whether an object is instance of a class.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public TemplateModel exec(List arguments)
      throws TemplateModelException {
    try {
      Object target = ((AdapterTemplateModel) arguments.get(0))
          .getAdaptedObject(Object.class);
      Class<?> clazz = Class.forName(((TemplateScalarModel) arguments.get(1))
          .getAsString());
      return new BooleanModel(clazz.isAssignableFrom(target
          .getClass()), wrapper);
    } catch (Exception ex) {
      throw new TemplateModelException("Could execute instanceof method.", ex);
    }
  }
}
