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

import org.jspresso.framework.model.descriptor.ISourceCodePropertyDescriptor;

/**
 * Describes a property as handing sourcecode content. This instructs Jspresso
 * to display the property value as sourcecode, using syntax coloring for
 * instance, instead of displaying un-formatted raw content. The language used to
 * format the property text content may be defined explicitly using the
 * {@code language} property.
 *
 * @author Vincent Vandenschrick
 */
public class BasicSourceCodePropertyDescriptor extends
    BasicTextPropertyDescriptor implements ISourceCodePropertyDescriptor {

  private String language;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicSourceCodePropertyDescriptor clone() {
    BasicSourceCodePropertyDescriptor clonedDescriptor = (BasicSourceCodePropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLanguage() {
    return language;
  }

  /**
   * Explicitly sets the language this sourcecode property should contain. This
   * is only a hint fo Jspresso to configure the UI components accordingly to
   * interact with this property.
   *
   * @param language
   *          the language to set.
   */
  public void setLanguage(String language) {
    this.language = language;
  }
}
