/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.gui.ulc.components.server;

import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jspresso.framework.gui.ulc.components.shared.DurationDataTypeConstants;

import com.ulcjava.base.application.ULCProxy;
import com.ulcjava.base.application.datatype.IDataType;
import com.ulcjava.base.shared.internal.Anything;

/**
 * An ULC datatype to provide duration renderers on ULCExtendedTable, ULCList,
 * ... They are used in conjunction with ULCLabels.
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
    return "org.jspresso.framework.gui.ulc.components.client.UIDurationDataType";
  }

}
