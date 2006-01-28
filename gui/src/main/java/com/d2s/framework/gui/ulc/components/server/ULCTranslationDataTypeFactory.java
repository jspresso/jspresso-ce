/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A factory to create (and cache) translation data types.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCTranslationDataTypeFactory {

  private Map<String, Map<String, Map<Locale, ULCTranslationDataType>>> dataTypeStore;

  /**
   * Constructs a new <code>ULCTranslationDataTypeFactory</code> instance.
   */
  public ULCTranslationDataTypeFactory() {
    dataTypeStore = new HashMap<String, Map<String, Map<Locale, ULCTranslationDataType>>>();
  }

  /**
   * Gets a (cached) translation data type.
   * 
   * @param bundle
   *          the resource bundle name on which the translation data type
   *          relies.
   * @param prefix
   *          the key prefix the translation data type uses.
   * @param locale
   *          the locale the translation data type uses.
   * @return the corresponding translation data type.
   */
  public ULCTranslationDataType getTranslationDataType(String bundle,
      String prefix, Locale locale) {
    Map<String, Map<Locale, ULCTranslationDataType>> bundleStore = dataTypeStore
        .get(bundle);
    if (bundleStore == null) {
      bundleStore = new HashMap<String, Map<Locale, ULCTranslationDataType>>();
      dataTypeStore.put(bundle, bundleStore);
    }
    Map<Locale, ULCTranslationDataType> prefixStore = bundleStore.get(prefix);
    if (prefixStore == null) {
      prefixStore = new HashMap<Locale, ULCTranslationDataType>();
      bundleStore.put(prefix, prefixStore);
    }
    ULCTranslationDataType dataType = prefixStore.get(locale);
    if (dataType == null) {
      dataType = new ULCTranslationDataType(bundle, prefix, locale);
      prefixStore.put(locale, dataType);
    }
    return dataType;
  }
}
