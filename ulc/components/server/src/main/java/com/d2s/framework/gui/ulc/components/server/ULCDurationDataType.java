/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.server;

import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.gui.ulc.components.shared.DurationDataTypeConstants;
import com.ulcjava.base.application.ULCProxy;
import com.ulcjava.base.application.datatype.IDataType;
import com.ulcjava.base.shared.internal.Anything;

/**
 * An ULC datatype to provide duration renderers on ULCExtendedTable, ULCList,
 * ... They are used in conjunction with ULCLabels.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCDurationDataType extends ULCProxy implements IDataType {

  private static final long serialVersionUID = 8987100370651810472L;

  private Locale            locale;

  /**
   * Constructs a new <code>ULCDurationDataType</code> instance.
   */
  public ULCDurationDataType() {
    this(null);
  }

  /**
   * Constructs a new <code>ULCDurationDataType</code> instance. This
   * constructor has default visibility to prevent for direct instanciation.
   * 
   * @param locale
   *            the locale for the translations.
   */
  ULCDurationDataType(Locale locale) {
    this.locale = locale;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ULCDurationDataType)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    ULCDurationDataType rhs = (ULCDurationDataType) obj;
    return new EqualsBuilder().append(locale, rhs.locale).isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(7, 23).append(locale).toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void saveState(Anything a) {
    super.saveState(a);
    saveState(a, DurationDataTypeConstants.LOCALE, locale.getLanguage(), null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "com.d2s.framework.gui.ulc.components.client.UIDurationDataType";
  }

}
