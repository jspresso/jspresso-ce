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

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringUtils;

/**
 * This abstract class serves as the base class for all JTextComponent
 * connectors.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          The actual class of the subclass of {@code JTextComponent}.
 */
public abstract class JTextComponentConnector<E extends JTextComponent> extends
    JComponentConnector<E> {

  private Color savedSelectedTextColor;

  /**
   * Constructs a new {@code JTextComponentConnector} instance. The
   * connector will listen to {@code focusLost} events so subclass only
   * need to listen to other unhandled events if necessary.
   *
   * @param id
   *          the connector identifier.
   * @param textComponent
   *          the connected JTextComponent.
   */
  public JTextComponentConnector(String id, E textComponent) {
    super(id, textComponent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindJComponent() {
    getConnectedJComponent().addFocusListener(new FocusAdapter() {

      /**
       * {@inheritDoc}
       */
      @Override
      public void focusLost(FocusEvent e) {
        if (!e.isTemporary()) {
          fireConnectorValueChange();
        }
      }
    });
  }

  /**
   * Gets the value out of the connector.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    String text = getConnectedJComponent().getText();
    if (StringUtils.isEmpty(text)) {
      return null;
    }
    return text;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void protectedReadabilityChange() {
    super.protectedReadabilityChange();
    if (isReadable()) {
      if (savedSelectedTextColor != null) {
        getConnectedJComponent().setSelectedTextColor(savedSelectedTextColor);
      }
      savedSelectedTextColor = null;
    } else if (savedSelectedTextColor == null) {
      savedSelectedTextColor = getConnectedJComponent().getSelectedTextColor();
      getConnectedJComponent().setSelectedTextColor(
          getConnectedJComponent().getSelectionColor());
    }
  }

  /**
   * Sets the value to the connector text.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void protectedSetConnecteeValue(Object aValue) {
    if (aValue == null) {
      getConnectedJComponent().setText(null);
    } else {
      getConnectedJComponent().setText(aValue.toString());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void protectedWritabilityChange() {
    super.protectedWritabilityChange();
    getConnectedJComponent().setEditable(isWritable());
  }
}
