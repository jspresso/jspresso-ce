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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.util.lang.StringUtils;

/**
 * Describes an enumerated property containing arbitrary values.
 *
 * @author Vincent Vandenschrick
 */
public class BasicEnumerationPropertyDescriptor extends
    AbstractEnumerationPropertyDescriptor {

  private Map<String, String> valuesAndIconImageUrls;

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicEnumerationPropertyDescriptor clone() {
    BasicEnumerationPropertyDescriptor clonedDescriptor = (BasicEnumerationPropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getEnumerationValues() {
    return new ArrayList<>(valuesAndIconImageUrls.keySet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIconImageURL(String value) {
    if (valuesAndIconImageUrls != null) {
      return valuesAndIconImageUrls.get(value);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTranslated() {
    return true;
  }

  /**
   * Defines the list of values this enumeration contains.
   * <p>
   * Enumeration values are translated in the UI using the following scheme :
   * <i>[enumerationName]_[value]</i>.
   *
   * @param values
   *          the values to set.
   */
  public void setValues(List<String> values) {
    valuesAndIconImageUrls = new LinkedHashMap<>();
    for (String value : StringUtils.ensureSpaceFree(values)) {
      valuesAndIconImageUrls.put(value, null);
    }
  }

  /**
   * Defines the list of values as well as an icon image URL per value this
   * enumeration contains. The incoming {@code Map} is keyed by the actual
   * enumeration values and valued by the icon image URLs.
   * <p>
   * Enumeration values are translated in the UI using the following scheme :
   * <i>[enumerationName]_[value]</i>.
   *
   * @param valuesAndIconImageUrls
   *          the valuesAndIconImageUrls to set.
   */
  public void setValuesAndIconImageUrls(
      Map<String, String> valuesAndIconImageUrls) {
    this.valuesAndIconImageUrls = valuesAndIconImageUrls;
  }


  /**
   * Returns true.
   * @return {@code true}
   */
  @Override
  public boolean isLov() {
    return false;
  }

  @Override
  public Object getDefaultValue() {
    Object defaultValue = super.getDefaultValue();
    if (defaultValue == null && isMandatory()) {
      defaultValue = getEnumerationValues().get(0);
    }
    return defaultValue;
  }
}
