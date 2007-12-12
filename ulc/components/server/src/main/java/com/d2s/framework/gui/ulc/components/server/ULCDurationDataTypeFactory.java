/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A factory to create (and cache) duration data types.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
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
