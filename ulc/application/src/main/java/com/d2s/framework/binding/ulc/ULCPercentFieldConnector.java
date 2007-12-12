/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.ulc;

import com.ulcjava.base.application.ULCTextField;

/**
 * ULCTextField connector dedicated to percentage handling. This is necessary
 * since ULCPercentDatatype returns the percent value as the field value instead
 * of dividing it per 100.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCPercentFieldConnector extends ULCTextFieldConnector {

  /**
   * Constructs a new <code>ULCPercentFieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param textField
   *            the connected ULCTextField.
   */
  public ULCPercentFieldConnector(String id, ULCTextField textField) {
    super(id, textField);
  }

  /**
   * Gets the value out of the connector text after parsing the string
   * representation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    if (getConnectedULCComponent().getValue() != null) {
      return new Double(((Number) getConnectedULCComponent().getValue())
          .doubleValue() / 100.0D);
    }
    return null;
  }

  /**
   * Sets the value to the connector text after formatting the string
   * representation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (aValue != null) {
      getConnectedULCComponent().setValue(
          new Double(((Number) aValue).doubleValue() * 100.0D));
    } else {
      super.setConnecteeValue(aValue);
    }
  }
}
