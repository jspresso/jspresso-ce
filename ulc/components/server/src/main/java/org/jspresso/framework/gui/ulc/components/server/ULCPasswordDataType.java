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

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.ulcjava.base.application.ULCProxy;
import com.ulcjava.base.application.datatype.IDataType;
import com.ulcjava.base.shared.internal.Anything;

/**
 * An ULC datatype to provide password renderers on ULCExtendedTable, ULCList,
 * ... They are used in conjunction with ULCLabels.
 * 
 * @version $LastChangedRevision: 2097 $
 * @author Vincent Vandenschrick
 */
public class ULCPasswordDataType extends ULCProxy implements IDataType {

  private static final long serialVersionUID = 7190155489491516113L;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ULCPasswordDataType)) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(7, 23).toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void saveState(Anything a) {
    super.saveState(a);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "org.jspresso.framework.gui.ulc.components.client.UIPasswordDataType";
  }

}
