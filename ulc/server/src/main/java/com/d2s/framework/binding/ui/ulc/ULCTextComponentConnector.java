/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.ui.ulc;

import com.ulcjava.base.application.ULCTextComponent;
import com.ulcjava.base.application.event.ValueChangedEvent;
import com.ulcjava.base.application.event.serializable.IValueChangedListener;
import com.ulcjava.base.application.util.Color;

/**
 * This abstract class serves as the base class for all ULCTextComponent
 * connectors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          The actual class of the subclass of <code>ULCTextComponent</code>.
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
   *          the connector identifier.
   * @param textComponent
   *          the connected ULCTextComponent.
   */
  public ULCTextComponentConnector(String id, E textComponent) {
    super(id, textComponent);
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateState() {
    super.updateState();
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
    getConnectedULCComponent().setEditable(isWritable());
  }
}
