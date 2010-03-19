/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.gui.ulc.components.client;

import java.text.ParseException;
import java.util.Locale;

import org.jspresso.framework.gui.ulc.components.shared.DurationDataTypeConstants;
import org.jspresso.framework.util.format.DurationFormatter;
import org.jspresso.framework.util.lang.ObjectUtils;

import com.ulcjava.base.client.datatype.DataTypeConversionException;
import com.ulcjava.base.client.datatype.UIDataType;
import com.ulcjava.base.shared.internal.Anything;

/**
 * An ULC datatype client UI to provide duration renderers on ULCExtendedTable,
 * ULCList, ... They are used in conjunction with ULCLabels.
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
