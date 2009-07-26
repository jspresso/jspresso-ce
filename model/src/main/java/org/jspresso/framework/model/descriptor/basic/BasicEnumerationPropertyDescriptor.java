/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

/**
 * Default implementation of an enumerationValues descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
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
  public List<String> getEnumerationValues() {
    return new ArrayList<String>(valuesAndIconImageUrls.keySet());
  }

  /**
   * {@inheritDoc}
   */
  public String getIconImageURL(String value) {
    if (valuesAndIconImageUrls != null) {
      return valuesAndIconImageUrls.get(value);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isTranslated() {
    return true;
  }

  /**
   * Sets the valuesAndIconImageUrls property.
   * 
   * @param valuesAndIconImageUrls
   *          the valuesAndIconImageUrls to set.
   */
  public void setValuesAndIconImageUrls(
      Map<String, String> valuesAndIconImageUrls) {
    this.valuesAndIconImageUrls = valuesAndIconImageUrls;
  }

  /**
   * Sets the values of the enumeration without icons.
   * 
   * @param values
   *          the values to set.
   */
  public void setValues(List<String> values) {
    valuesAndIconImageUrls = new LinkedHashMap<String, String>();
    for (String value : values) {
      valuesAndIconImageUrls.put(value, null);
    }
  }

}
