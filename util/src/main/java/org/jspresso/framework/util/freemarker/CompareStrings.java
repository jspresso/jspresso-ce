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
import freemarker.ext.beans.NumberModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 * Compares two strings using java rules.
 *
 * @author Vincent Vandenschrick
 */
@SuppressWarnings("rawtypes")
public class CompareStrings implements TemplateMethodModelEx {

  private final BeansWrapper wrapper;

  /**
   * Constructs a new {@code CompareStrings} instance.
   *
   * @param wrapper
   *          the beans wrapper.
   */
  public CompareStrings(BeansWrapper wrapper) {
    this.wrapper = wrapper;
  }

  /**
   * Compares two strings using java rules.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public TemplateModel exec(List arguments)
      throws TemplateModelException {
    try {
      String first = ((TemplateScalarModel) arguments.get(0)).getAsString();
      String second = ((TemplateScalarModel) arguments.get(1)).getAsString();
      return new NumberModel(first.compareTo(second), wrapper);
    } catch (Exception ex) {
      throw new TemplateModelException("Could execute compareStrings method.",
          ex);
    }
  }
}
