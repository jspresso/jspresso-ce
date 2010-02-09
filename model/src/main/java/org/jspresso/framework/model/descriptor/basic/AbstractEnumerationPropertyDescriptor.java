/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;

/**
 * Abstract base descriptor for properties whose values are enumerated strings.
 * An example of such a property is <i>gender</i> whose value can be <i>M</i>
 * (for &quot;Male&quot;) or <i>F</i> (for &quot;Female&quot;). Actual property
 * values can be codes that are translated for inclusion in the UI. Such
 * properties are usually rendered as combo-boxes.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractEnumerationPropertyDescriptor extends
    BasicScalarPropertyDescriptor implements IEnumerationPropertyDescriptor {

  private String  enumerationName;
  private Integer maxLength;

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractEnumerationPropertyDescriptor clone() {
    AbstractEnumerationPropertyDescriptor clonedDescriptor = (AbstractEnumerationPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public String getEnumerationName() {
    return enumerationName;
  }

  /**
   * Gets the maxLength.
   * 
   * @return the maxLength.
   */
  public Integer getMaxLength() {
    if (maxLength != null) {
      return maxLength;
    }
    int max = 1;
    for (String value : getEnumerationValues()) {
      if (value != null && value.length() > max) {
        max = value.length();
      }
    }
    return new Integer(max);
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return String.class;
  }

  /**
   * This property allows to customize the i18n keys used to translate the
   * enumeration values, thus keeping the actual values shorter. For instance
   * consider the <i>gender</i> enumeration, composed of the <i>M</i> (for
   * &quot;Male&quot;) and <i>F</i> (for &quot;Female&quot;) values. Setting an
   * enumeration name to &quot;GENDER&quot; will instruct Jspresso to look for
   * translations named &quot;GENDER_M&quot; and &quot;GENDER_F&quot;. This
   * would allow for using <i>M</i> and <i>F</i> in other enumeration domains
   * with different semantics and translations.
   * 
   * @param enumerationName
   *          the enumerationName to set.
   */
  public void setEnumerationName(String enumerationName) {
    this.enumerationName = enumerationName;
  }

  /**
   * Sets the maxLength.
   * 
   * @param maxLength
   *          the maxLength to set.
   */
  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }
}
