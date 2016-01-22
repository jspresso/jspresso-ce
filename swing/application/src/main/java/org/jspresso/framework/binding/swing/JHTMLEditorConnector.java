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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jspresso.framework.gui.swing.components.JHTMLEditor;

/**
 * This class is a JHTMLEditor connector.
 *
 * @author Vincent Vandenschrick
 */
public class JHTMLEditorConnector extends JComponentConnector<JHTMLEditor> {

  private boolean isUserAction;

  /**
   * Constructs a new {@code JHTMLEditorConnector} instance. The connector
   * will listen to {@code focusLost} events.
   *
   * @param id
   *          the connector identifier.
   * @param htmlEditor
   *          the connected JTextComponent.
   */
  public JHTMLEditorConnector(String id, JHTMLEditor htmlEditor) {
    super(id, htmlEditor);
    isUserAction = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindJComponent() {
    getConnectedJComponent().getEditorPane().addFocusListener(
        new FocusAdapter() {

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

    getConnectedJComponent().getEditorPane().getDocument()
        .addDocumentListener(new DocumentListener() {

          @Override
          public void changedUpdate(DocumentEvent e) {
            documentChanged(e);
          }

          @Override
          public void insertUpdate(DocumentEvent e) {
            documentChanged(e);
          }

          @Override
          public void removeUpdate(DocumentEvent e) {
            documentChanged(e);
          }

          @SuppressWarnings({"unused", "UnusedParameters"})
          private void documentChanged(DocumentEvent e) {
            if (isUserAction
                && !getConnectedJComponent().getEditorPane().hasFocus()) {
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
    return getConnectedJComponent().getText();
  }

  /**
   * Sets the value to the connector text.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void protectedSetConnecteeValue(Object aValue) {
    boolean wasUserAction = isUserAction;
    try {
      isUserAction = false;
      if (aValue == null) {
        getConnectedJComponent().setText(null);
      } else {
        getConnectedJComponent().setText(aValue.toString());
      }
    } finally {
      isUserAction = wasUserAction;
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
