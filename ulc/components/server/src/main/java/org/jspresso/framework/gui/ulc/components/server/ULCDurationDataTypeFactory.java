/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.gui.ulc.components.server;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A factory to create (and cache) duration data types.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCDurationDataTypeFactory {

  private Map<Locale, ULCDurationDataType> dataTypeStore;

  /**
   * Constructs a new <code>ULCTranslationDataTypeFactory</code> instance.
   */
  public ULCDurationDataTypeFactory() {
    dataTypeStore = new HashMap<Locale, ULCDurationDataType>();
  }

  /**
   * Gets a (cached) duration data type.
   * 
   * @param locale
   *            the locale the duration data type uses.
   * @return the corresponding duration data type.
   */
  public ULCDurationDataType getTranslationDataType(Locale locale) {
    ULCDurationDataType dataType = dataTypeStore.get(locale);
    if (dataType == null) {
      dataType = new ULCDurationDataType(locale);
      dataTypeStore.put(locale, dataType);
    }
    return dataType;
  }
}
