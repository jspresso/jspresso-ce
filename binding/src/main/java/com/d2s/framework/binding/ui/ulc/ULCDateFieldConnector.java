/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.ui.ulc;

import java.util.Date;

import com.d2s.framework.view.ulc.components.server.ULCDateField;
import com.ulcjava.base.application.event.ValueChangedEvent;
import com.ulcjava.base.application.event.serializable.IValueChangedListener;

/**
 * ULCDateField connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCDateFieldConnector extends ULCComponentConnector<ULCDateField> {

  /**
   * Constructs a new <code>ULCDateFieldConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param dateField
   *          the connected ULCDateField.
   */
  public ULCDateFieldConnector(String id, ULCDateField dateField) {
    super(id, dateField);
  }

  /**
   * Returns a <code>Date</code> object mapping the state of the date field.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getConnectedULCComponent().getValue();
  }

  /**
   * Set the state of the date field depending on the connector value to set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    getConnectedULCComponent().setValue((Date) aValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindULCComponent() {
    getConnectedULCComponent().addValueChangedListener(
        new IValueChangedListener() {

          private static final long serialVersionUID = -7747055134828532559L;

          public void valueChanged(@SuppressWarnings("unused")
          ValueChangedEvent event) {
            fireConnectorValueChange();
          }
        });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateState() {
    super.updateState();
    getConnectedULCComponent().setEditable(isWritable());
  }

}
