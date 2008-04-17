/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.wings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import org.wingx.XCalendar;

/**
 * XCalendar connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class XCalendarConnector extends SComponentConnector<XCalendar> {

  /**
   * Constructs a new <code>XCalendar</code> instance.
   * 
   * @param id
   *            the connector identifier.
   * @param dateField
   *            the connected XCalendar.
   */
  public XCalendarConnector(String id, XCalendar dateField) {
    super(id, dateField);
  }

  /**
   * Sets the value to the connector text after formatting the string
   * representation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setConnecteeValue(Object aValue) {
    getConnectedSComponent().setDate((Date) aValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateState() {
    super.updateState();
    getConnectedSComponent().setEnabled(isWritable());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindSComponent() {
    getConnectedSComponent().addActionListener(new ActionListener() {

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        fireConnectorValueChange();
      }

    });
  }

  /**
   * Gets the value out of the connector text after parsing the string
   * representation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getConnectedSComponent().getDate();
  }
}
