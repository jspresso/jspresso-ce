/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.client;

import java.util.Locale;

import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;

import com.d2s.framework.gui.ulc.components.shared.DurationDataTypeConstants;
import com.d2s.framework.util.lang.ObjectUtils;
import com.ulcjava.base.client.datatype.UIDataType;
import com.ulcjava.base.shared.internal.Anything;

/**
 * An ULC datatype client UI to provide duration renderers on ULCExtendedTable,
 * ULCList, ... They are used in conjunction with ULCLabels.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class UIDurationDataType extends UIDataType {

  private PeriodFormatter formatter;
  private Locale          locale;

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
    formatter = PeriodFormat.getDefault();
    if (locale != null) {
      formatter = formatter.withLocale(locale);
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
    return new Long(formatter.parsePeriod(newString).toDurationFrom(
        new Instant(0)).getMillis());
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
    return formatter.print(new Period(0, ((Number) object).longValue()));
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
  public int hashCode() {
    if (locale == null) {
      return 0;
    }
    return locale.hashCode();
  }
}
