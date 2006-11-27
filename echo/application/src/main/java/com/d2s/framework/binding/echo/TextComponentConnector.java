/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.echo;

import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.event.DocumentEvent;
import nextapp.echo2.app.event.DocumentListener;
import nextapp.echo2.app.text.TextComponent;

import org.apache.commons.lang.StringUtils;

/**
 * This abstract class serves as the base class for all TextComponent
 * connectors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          The actual class of the subclass of <code>TextComponent</code>.
 */
public abstract class TextComponentConnector<E extends TextComponent> extends
    ComponentConnector<E> {

  /**
   * Constructs a new <code>TextComponentConnector</code> instance. The
   * connector will listen to <code>focusLost</code> events so subclass only
   * need to listen to other unhandled events if necessary.
   *
   * @param id
   *          the connector identifier.
   * @param textComponent
   *          the connected JTextComponent.
   */
  public TextComponentConnector(String id, E textComponent) {
    super(id, textComponent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindComponent() {
    getConnectedComponent().addActionListener(new ActionListener() {

      private static final long serialVersionUID = 6414718987774762905L;

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        fireConnectorValueChange();
      }
    });
    getConnectedComponent().getDocument().addDocumentListener(
        new DocumentListener() {

          private static final long serialVersionUID = 6274371393680770020L;

          public void documentUpdate(@SuppressWarnings("unused")
          DocumentEvent e) {
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
    String text = getConnectedComponent().getText();
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
      getConnectedComponent().setText(null);
    } else {
      getConnectedComponent().setText(aValue.toString());
    }
  }
}
