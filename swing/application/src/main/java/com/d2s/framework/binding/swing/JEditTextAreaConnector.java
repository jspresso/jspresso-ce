/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import org.syntax.jedit.JEditTextArea;

/**
 * This class is a JEditTextArea connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JEditTextAreaConnector extends JComponentConnector<JEditTextArea> {

  /**
   * Constructs a new <code>JEditTextAreaConnector</code> instance. The
   * connector will listen to <code>focusLost</code> events.
   * 
   * @param id
   *            the connector identifier.
   * @param textArea
   *            the connected JTextComponent.
   */
  public JEditTextAreaConnector(String id, JEditTextArea textArea) {
    super(id, textArea);
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
    return getConnectedJComponent().getText();
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
  protected void protectedUpdateState() {
    super.protectedUpdateState();
    getConnectedJComponent().setEditable(isWritable());
  }
}
