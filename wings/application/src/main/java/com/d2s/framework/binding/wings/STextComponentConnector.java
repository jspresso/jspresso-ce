/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.wings;

import java.awt.Color;

import org.apache.commons.lang.StringUtils;
import org.wings.STextComponent;
import org.wings.event.SDocumentEvent;
import org.wings.event.SDocumentListener;

/**
 * This abstract class serves as the base class for all STextComponent
 * connectors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          The actual class of the subclass of <code>STextComponent</code>.
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
   *          the connector identifier.
   * @param textComponent
   *          the connected STextComponent.
   */
  public STextComponentConnector(String id, E textComponent) {
    super(id, textComponent);
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateState() {
    super.updateState();
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
    getConnectedSComponent().setEditable(isWritable());
  }
}
