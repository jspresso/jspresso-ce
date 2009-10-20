/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import java.text.ParseException;

import org.jspresso.framework.util.format.IFormatter;

import com.ulcjava.base.application.ULCTextField;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.serializable.IActionListener;

/**
 * ULCTextField connector. Instances of this class must be provided with a
 * <code>IDataType</code>. If not set, the string representation contained in
 * the text component will be used "as is" as the connector value.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCTextFieldConnector extends
    ULCTextComponentConnector<ULCTextField> {

  private IFormatter formatter;

  /**
   * Constructs a new <code>ULCTextFieldConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param textField
   *          the connected ULCTextField.
   */
  public ULCTextFieldConnector(String id, ULCTextField textField) {
    super(id, textField);
  }

  /**
   * Sets the formatter.
   * 
   * @param formatter
   *          the formatter to set.
   */
  public void setFormatter(IFormatter formatter) {
    this.formatter = formatter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindULCComponent() {
    super.bindULCComponent();
    getConnectedULCComponent().addActionListener(new IActionListener() {

      private static final long serialVersionUID = 5283965145691569361L;

      /**
       * {@inheritDoc}
       */
      public void actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
        fireConnectorValueChange();
      }
    });
  }

  /**
   * Gets the value out of the connector text after parsing the string
   * representation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    Object value;
    if (formatter != null) {
      try {
        value = formatter.parse(getConnectedULCComponent().getText());
      } catch (ParseException pe) {
        // value could not be parsed and thus is invalid.
        // restore old value.
        value = getOldConnectorValue();
      }
      setConnecteeValue(value);
    } else {
      value = super.getConnecteeValue(); 
    }
    return value;
  }

  /**
   * Sets the value to the connector text after formatting the string
   * representation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (formatter != null) {
      getConnectedULCComponent().setText(formatter.format(aValue));
    } else {
      super.setConnecteeValue(aValue);
    }
  }
}
