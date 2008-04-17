/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.ulc;

import org.jspresso.framework.gui.ulc.components.server.ULCJEditTextArea;

import com.ulcjava.base.application.event.ValueChangedEvent;
import com.ulcjava.base.application.event.serializable.IValueChangedListener;

/**
 * This class is a ULCJEditTextArea connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCJEditTextAreaConnector extends
    ULCComponentConnector<ULCJEditTextArea> {

  /**
   * Constructs a new <code>ULCJEditTextAreaConnector</code> instance. The
   * connector will listen to <code>focusLost</code> events.
   * 
   * @param id
   *            the connector identifier.
   * @param textArea
   *            the connected JTextComponent.
   */
  public ULCJEditTextAreaConnector(String id, ULCJEditTextArea textArea) {
    super(id, textArea);
  }

  /**
   * Sets the value to the connector text.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setConnecteeValue(Object aValue) {
    if (aValue == null) {
      getConnectedULCComponent().setText(null);
    } else {
      getConnectedULCComponent().setText(aValue.toString());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateState() {
    super.updateState();
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
    return getConnectedULCComponent().getText();
  }
}
