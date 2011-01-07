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
package org.jspresso.framework.binding.ulc;

import com.ulcjava.base.application.ULCTextField;

/**
 * ULCTextField connector dedicated to percentage handling. This is necessary
 * since ULCPercentDatatype returns the percent value as the field value instead
 * of dividing it per 100.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCPercentFieldConnector extends ULCTextFieldConnector {

  /**
   * Constructs a new <code>ULCPercentFieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param textField
   *            the connected ULCTextField.
   */
  public ULCPercentFieldConnector(String id, ULCTextField textField) {
    super(id, textField);
  }

  /**
   * Gets the value out of the connector text after parsing the string
   * representation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    if (getConnectedULCComponent().getValue() != null) {
      return new Double(((Number) getConnectedULCComponent().getValue())
          .doubleValue() / 100.0D);
    }
    return null;
  }

  /**
   * Sets the value to the connector text after formatting the string
   * representation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (aValue != null) {
      getConnectedULCComponent().setValue(
          new Double(((Number) aValue).doubleValue() * 100.0D));
    } else {
      super.setConnecteeValue(aValue);
    }
  }
}
