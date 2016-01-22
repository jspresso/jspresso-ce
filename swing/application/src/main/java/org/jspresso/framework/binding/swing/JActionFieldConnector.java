/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding.swing;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import org.apache.commons.lang3.StringUtils;
import org.jspresso.framework.gui.swing.components.JActionField;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;

/**
 * JActionFieldConnector connector.
 *
 * @author Vincent Vandenschrick
 */
public class JActionFieldConnector extends JComponentConnector<JActionField> {

  private Object value;

  /**
   * Constructs a new {@code JActionFieldConnector} instance.
   *
   * @param id
   *          the id of the connector.
   * @param actionField
   *          the connected JActionField.
   */
  public JActionFieldConnector(String id, JActionField actionField) {
    super(id, actionField);
    if (!getConnectedJComponent().isShowingTextField()) {
      addValueChangeListener(new IValueChangeListener() {

        @Override
        public void valueChange(ValueChangeEvent evt) {
          if (evt.getNewValue() instanceof byte[]) {
            getConnectedJComponent().setDecorated(
                ((byte[]) evt.getNewValue()).length > 0);
          } else {
            getConnectedJComponent().setDecorated(evt.getNewValue() != null);
          }
        }
      });
    }
  }

  /**
   * Performs the action field action if the action field is not synchronized.
   */
  public void performActionIfNeeded() {
    if (!getConnectedJComponent().isSynchronized()) {
      if (StringUtils.isEmpty(getConnectedJComponent().getActionText())) {
        setConnectorValue(null);
      } else {
        getConnectedJComponent().performAction();
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindJComponent() {

    getConnectedJComponent().addTextFieldFocusListener(new FocusAdapter() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void focusLost(FocusEvent e) {
        if (!e.isTemporary()) {
          performActionIfNeeded();
        }
      }
    });
  }

  /**
   * Gets the action text to display in the action field.
   *
   * @return the action text to display in the action field.
   */
  protected String getActionText() {
    if (value == null) {
      return "";
    }
    return value.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void protectedSetConnecteeValue(Object aValue) {
    value = aValue;
    getConnectedJComponent().setValue(getActionText());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void protectedWritabilityChange() {
    super.protectedWritabilityChange();
    getConnectedJComponent().setEnabled(isWritable());
  }
}
