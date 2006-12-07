/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.echo;

import java.util.Calendar;
import java.util.Date;

import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.event.DocumentEvent;
import nextapp.echo2.app.event.DocumentListener;
import echopointng.DateField;

/**
 * DateField connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DateFieldConnector extends ComponentConnector<DateField> {

  /**
   * Constructs a new <code>JDateField</code> instance.
   * 
   * @param id
   *          the connector identifier.
   * @param dateField
   *          the connected DateField.
   */
  public DateFieldConnector(String id, DateField dateField) {
    super(id, dateField);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindComponent() {
    getConnectedComponent().getTextField().addActionListener(
        new ActionListener() {

          private static final long serialVersionUID = 6414718987774762905L;

          public void actionPerformed(@SuppressWarnings("unused")
          ActionEvent e) {
            fireConnectorValueChange();
          }
        });
    getConnectedComponent().getTextField().getDocument().addDocumentListener(
        new DocumentListener() {

          private static final long serialVersionUID = 6274371393680770020L;

          public void documentUpdate(@SuppressWarnings("unused")
          DocumentEvent e) {
            fireConnectorValueChange();
          }
        });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getConnectedComponent().getDisplayedDate().getTime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object connecteeValue) {
    if (connecteeValue != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime((Date) connecteeValue);
      getConnectedComponent().setDisplayedDate(cal);
    } else {
      getConnectedComponent().setDisplayedDate(null);
    }
  }
}
