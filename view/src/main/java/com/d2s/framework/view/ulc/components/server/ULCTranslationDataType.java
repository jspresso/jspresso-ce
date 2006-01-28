/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.ulc.components.server;

import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.view.ulc.components.shared.TranslationDataTypeConstants;
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

  private static final long serialVersionUID = 5080679351626979457L;

  private String            bundle;
  private String            prefix;
  private Locale            locale;

  /**
   * Constructs a new <code>ULCTranslationDataType</code> instance. This
   * constructor has default visibility to prevent for direct instanciation.
   * 
   * @param bundle
   *          the name of the resource bundle to use for the translation.
   * @param prefix
   *          the prefix to use for the translation.
   * @param locale
   *          the locale to use for the translation.
   */
  ULCTranslationDataType(String bundle, String prefix, Locale locale) {
    this.bundle = bundle;
    this.prefix = prefix;
    this.locale = locale;
  }

  /**
   * Constructs a new <code>ULCTranslationDataType</code> instance.
   */
  public ULCTranslationDataType() {
    this(null, null, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void saveState(Anything a) {
    super.saveState(a);
    saveState(a, TranslationDataTypeConstants.BUNDLE_KEY, bundle, null);
    saveState(a, TranslationDataTypeConstants.PREFIX_KEY, prefix, null);
    saveState(a, TranslationDataTypeConstants.LANGUAGE_KEY, locale
        .getLanguage(), null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "com.d2s.framework.view.ulc.components.client.UITranslationDataType";
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
