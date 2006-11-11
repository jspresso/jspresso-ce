/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.d2s.framework.gui.ulc.components.shared.TranslationDataTypeConstants;
import com.d2s.framework.util.lang.ObjectUtils;
import com.ulcjava.base.client.datatype.UIDataType;
import com.ulcjava.base.shared.internal.Anything;

/**
 * An ULC datatype client UI to provide translated renderers on ULCExtendedTable,
 * ULCList, ... They are used in conjunction with ULCLabels.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UITranslationDataType extends UIDataType {

  private Map<String, String> dictionary;
  private Map<String, String> reverseDictionary;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void restoreState(Anything args) {
    super.restoreState(args);

    dictionary = new HashMap<String, String>();
    reverseDictionary = new HashMap<String, String>();

    Vector<String> flatDictionary = new Vector<String>();

    if (args.isDefined(TranslationDataTypeConstants.DICTIONARY)) {
      flatDictionary = args.get(TranslationDataTypeConstants.DICTIONARY)
          .toCollection();
    }
    for (int index = 0; index < flatDictionary.size() - 1; index += 2) {
      dictionary.put(flatDictionary.get(index), flatDictionary.get(index + 1));
      reverseDictionary.put(flatDictionary.get(index + 1), flatDictionary
          .get(index));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object convertToObject(String newString, @SuppressWarnings("unused")
  Object previousValue) {
    if (newString == null) {
      return null;
    }
    if (reverseDictionary == null) {
      return newString.toString();
    }
    return reverseDictionary.get(newString);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String convertToString(Object object, @SuppressWarnings("unused")
  boolean forEditing) {
    if (object == null) {
      return "";
    }
    if (dictionary == null) {
      return object.toString();
    }
    return dictionary.get(object);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getDefaultValue(String newString) {
    return newString;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof UITranslationDataType)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    UITranslationDataType rhs = (UITranslationDataType) obj;
    return ObjectUtils.equals(dictionary, rhs.dictionary);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    if (dictionary == null) {
      return 0;
    }
    return dictionary.hashCode();
  }
}
