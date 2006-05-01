/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.gui.ulc.components.shared.TranslationDataTypeConstants;
import com.ulcjava.base.application.ULCProxy;
import com.ulcjava.base.application.datatype.IDataType;
import com.ulcjava.base.shared.internal.Anything;

/**
 * An ULC datatype to provide translated renderers on ULCTable, ULCList, ...
 * They are used in conjunction with ULCLabels.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCTranslationDataType extends ULCProxy implements IDataType {

  private static final long   serialVersionUID = 5080679351626979457L;

  private Map<String, String> dictionary;

  /**
   * Constructs a new <code>ULCTranslationDataType</code> instance. This
   * constructor has default visibility to prevent for direct instanciation.
   * 
   * @param dictionary
   *          the dictionary containing the translations.
   */
  ULCTranslationDataType(Map<String, String> dictionary) {
    this.dictionary = dictionary;
  }

  /**
   * Constructs a new <code>ULCTranslationDataType</code> instance.
   */
  public ULCTranslationDataType() {
    this(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void saveState(Anything a) {
    super.saveState(a);
    Vector<String> flatDictionary = new Vector<String>();
    for (Map.Entry<String, String> dictEntry : dictionary.entrySet()) {
      flatDictionary.add(dictEntry.getKey());
      flatDictionary.add(dictEntry.getValue());
    }

    saveState(a, TranslationDataTypeConstants.DICTIONARY, flatDictionary, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "com.d2s.framework.gui.ulc.components.client.UITranslationDataType";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ULCTranslationDataType)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    ULCTranslationDataType rhs = (ULCTranslationDataType) obj;
    return new EqualsBuilder().append(dictionary, rhs.dictionary).isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(7, 23).append(dictionary).toHashCode();
  }

}
