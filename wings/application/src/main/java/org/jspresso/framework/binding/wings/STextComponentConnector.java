/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding.wings;

import java.awt.Color;

import org.apache.commons.lang.StringUtils;
import org.wings.STextComponent;
import org.wings.event.SDocumentEvent;
import org.wings.event.SDocumentListener;

/**
 * This abstract class serves as the base class for all STextComponent
 * connectors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 * @param <E>
 *            The actual class of the subclass of <code>STextComponent</code>.
 */
public abstract class STextComponentConnector<E extends STextComponent> extends
    SComponentConnector<E> {

  private Color savedSelectedTextColor;

  /**
   * Constructs a new <code>STextComponentConnector</code> instance. The
   * connector will listen to <code>focusLost</code> events so subclass only
   * need to listen to other unhandled events if necessary.
   * 
   * @param id
   *            the connector identifier.
   * @param textComponent
   *            the connected STextComponent.
   */
  public STextComponentConnector(String id, E textComponent) {
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
        getConnectedSComponent().setForeground(savedSelectedTextColor);
      }
      savedSelectedTextColor = null;
    } else if (savedSelectedTextColor == null) {
      savedSelectedTextColor = getConnectedSComponent().getForeground();
      getConnectedSComponent().setForeground(
          getConnectedSComponent().getForeground());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writabilityChange() {
    super.writabilityChange();
    getConnectedSComponent().setEditable(isWritable());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindSComponent() {
    getConnectedSComponent().addDocumentListener(new SDocumentListener() {

      public void changedUpdate(@SuppressWarnings("unused")
      SDocumentEvent e) {
        fireConnectorValueChange();
      }

      public void insertUpdate(@SuppressWarnings("unused")
      SDocumentEvent e) {
        fireConnectorValueChange();
      }

      public void removeUpdate(@SuppressWarnings("unused")
      SDocumentEvent e) {
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
    String text = getConnectedSComponent().getText();
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
      getConnectedSComponent().setText(null);
    } else {
      getConnectedSComponent().setText(aValue.toString());
    }
  }
}
