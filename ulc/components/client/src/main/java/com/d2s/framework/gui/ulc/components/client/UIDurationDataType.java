/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.client;

import java.text.ParseException;
import java.util.Locale;

import com.d2s.framework.gui.ulc.components.shared.DurationDataTypeConstants;
import com.d2s.framework.util.format.DurationFormatter;
import com.d2s.framework.util.lang.ObjectUtils;
import com.ulcjava.base.client.datatype.DataTypeConversionException;
import com.ulcjava.base.client.datatype.UIDataType;
import com.ulcjava.base.shared.internal.Anything;

/**
 * An ULC datatype client UI to provide duration renderers on ULCExtendedTable,
 * ULCList, ... They are used in conjunction with ULCLabels.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UIDurationDataType extends UIDataType {

  private DurationFormatter formatter;
  private Locale            locale;

  /**
   * {@inheritDoc}
   */
  @Override
  public Object convertToObject(String newString, @SuppressWarnings("unused")
  Object previousValue) throws DataTypeConversionException {
    try {
      return formatter.parse(newString);
    } catch (ParseException ex) {
      throw new DataTypeConversionException(ex.getMessage(), newString);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String convertToString(Object object, @SuppressWarnings("unused")
  boolean forEditing) {
    return formatter.format(object);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof UIDurationDataType)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    UIDurationDataType rhs = (UIDurationDataType) obj;
    return ObjectUtils.equals(locale, rhs.locale);
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
  public int hashCode() {
    if (locale == null) {
      return 0;
    }
    return locale.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void restoreState(Anything args) {
    super.restoreState(args);

    if (args.isDefined(DurationDataTypeConstants.LOCALE)) {
      locale = new Locale(args.get(DurationDataTypeConstants.LOCALE, Locale
          .getDefault().getLanguage()));
    }
    formatter = new DurationFormatter(locale);
  }
}
