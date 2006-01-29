/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.client;

import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.gui.ulc.components.shared.TranslationDataTypeConstants;
import com.d2s.framework.util.i18n.ITranslationProvider;
import com.d2s.framework.util.i18n.mock.MockTranslationProvider;
import com.ulcjava.base.client.datatype.UIDataType;
import com.ulcjava.base.shared.internal.Anything;

/**
 * An ULC datatype client UI to provide translated renderers on ULCTable,
 * ULCList, ... They are used in conjunction with ULCLabels.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UITranslationDataType extends UIDataType {

  private String               bundle;
  private String               prefix;
  private Locale               locale;

  private ITranslationProvider translationProvider;

  /**
   * {@inheritDoc}
   */
  @Override
  public void restoreState(Anything args) {
    super.restoreState(args);
    if (args.isDefined(TranslationDataTypeConstants.BUNDLE_KEY)) {
      bundle = args.get(TranslationDataTypeConstants.BUNDLE_KEY, (String) null);
    }
    if (args.isDefined(TranslationDataTypeConstants.PREFIX_KEY)) {
      prefix = args.get(TranslationDataTypeConstants.PREFIX_KEY, (String) null);
    }
    if (args.isDefined(TranslationDataTypeConstants.LANGUAGE_KEY)) {
      locale = new Locale(args.get(TranslationDataTypeConstants.LANGUAGE_KEY,
          (String) null));
      translationProvider = new MockTranslationProvider();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object convertToObject(String newString, @SuppressWarnings("unused")
  Object previousValue) {
    return newString;
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
    if (translationProvider == null) {
      return object.toString();
    }
    if (object.toString().startsWith("[")) {
      return object.toString();
    }
    StringBuffer key = new StringBuffer();
    if (prefix != null && prefix.length() > 0) {
      key.append(prefix).append(".");
    }
    key.append(object);
    return translationProvider.getTranslation(key.toString(), locale);
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
    return new EqualsBuilder().append(bundle, rhs.bundle).append(prefix,
        rhs.prefix).append(locale, rhs.locale).isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(7, 23).append(bundle).append(prefix).append(
        locale).toHashCode();
  }
}
