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
 * A factory to create (and cache) translation data types.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCTranslationDataTypeFactory {

  private Map<String, Map<Locale, ULCTranslationDataType>> dataTypeStore;

  /**
   * Constructs a new <code>ULCTranslationDataTypeFactory</code> instance.
   */
  public ULCTranslationDataTypeFactory() {
    dataTypeStore = new HashMap<String, Map<Locale, ULCTranslationDataType>>();
  }

  /**
   * Gets a (cached) translation data type.
   * 
   * @param id
   *            the id the translation data type is (or has been) stored with.
   * @param locale
   *            the locale the translation data type uses.
   * @param translationMapping
   *            key / value translation mapping used whenever the translation
   *            datatype does not exist yet and must be created.
   * @return the corresponding translation data type.
   */
  public ULCTranslationDataType getTranslationDataType(String id,
      Locale locale, Map<String, String> translationMapping) {
    Map<Locale, ULCTranslationDataType> idStore = dataTypeStore.get(id);
    if (idStore == null) {
      idStore = new HashMap<Locale, ULCTranslationDataType>();
      dataTypeStore.put(id, idStore);
    }
    ULCTranslationDataType dataType = idStore.get(locale);
    if (dataType == null) {
      dataType = new ULCTranslationDataType(translationMapping);
      idStore.put(locale, dataType);
    }
    return dataType;
  }
}
