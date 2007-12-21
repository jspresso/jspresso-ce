/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A factory to create (and cache) translation data types.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
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
