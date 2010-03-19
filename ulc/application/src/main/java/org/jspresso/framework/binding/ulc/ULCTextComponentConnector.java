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
package org.jspresso.framework.binding.ulc;

import org.apache.commons.lang.StringUtils;

import com.ulcjava.base.application.ULCTextComponent;
import com.ulcjava.base.application.event.ValueChangedEvent;
import com.ulcjava.base.application.event.serializable.IValueChangedListener;
import com.ulcjava.base.application.util.Color;

/**
 * This abstract class serves as the base class for all ULCTextComponent
 * connectors.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            The actual class of the subclass of <code>ULCTextComponent</code>.
 */
public abstract class ULCTextComponentConnector<E extends ULCTextComponent>
    extends ULCComponentConnector<E> {

  private Color savedSelectedTextColor;

  /**
   * Constructs a new <code>ULCTextComponentConnector</code> instance. The
   * connector will listen to <code>focusLost</code> events so subclass only
   * need to listen to other unhandled events if necessary.
   * 
   * @param id
   *            the connector identifier.
   * @param textComponent
   *            the connected ULCTextComponent.
   */
  public ULCTextComponentConnector(String id, E textComponent) {
    super(id, textComponent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readabilityChange() {
    super.readabilityChange();
    if (isReadable()) {
      if (savedSelectedTextColor != null) {
        getConnectedULCComponent().setSelectedTextColor(savedSelectedTextColor);
      }
      savedSelectedTextColor = null;
    } else if (savedSelectedTextColor == null) {
      savedSelectedTextColor = getConnectedULCComponent()
          .getSelectedTextColor();
      getConnectedULCComponent().setSelectedTextColor(
          getConnectedULCComponent().getSelectionColor());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writabilityChange() {
    super.writabilityChange();
    getConnectedULCComponent().setEditable(isWritable());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindULCComponent() {
    getConnectedULCComponent().addValueChangedListener(
        new IValueChangedListener() {

          private static final long serialVersionUID = 6575861898933203437L;

          /**
           * {@inheritDoc}
           */
          public void valueChanged(@SuppressWarnings("unused")
          ValueChangedEvent event) {
            fireConnectorValueChange();
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
    String text = getConnectedULCComponent().getText();
    if (StringUtils.isEmpty(text)) {
      return null;
    }
    return text;
  }

  /**
   * Sets the value to the connector text.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (aValue == null) {
      getConnectedULCComponent().setText(null);
    } else {
      getConnectedULCComponent().setText(aValue.toString());
    }
  }
}
