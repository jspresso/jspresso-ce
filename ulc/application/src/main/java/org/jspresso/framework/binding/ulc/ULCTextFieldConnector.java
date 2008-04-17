/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.ulc;

import com.ulcjava.base.application.ULCTextField;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.serializable.IActionListener;

/**
 * ULCTextField connector. Instances of this class must be provided with a
 * <code>IDataType</code>. If not set, the string representation contained in
 * the text component will be used "as is" as the connector value.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCTextFieldConnector extends
    ULCTextComponentConnector<ULCTextField> {

  /**
   * Constructs a new <code>ULCTextFieldConnector</code> instance.
   * 
   * @param id
   *            the id of the connector.
   * @param textField
   *            the connected ULCTextField.
   */
  public ULCTextFieldConnector(String id, ULCTextField textField) {
    super(id, textField);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindULCComponent() {
    super.bindULCComponent();
    getConnectedULCComponent().addActionListener(new IActionListener() {

      private static final long serialVersionUID = 5283965145691569361L;

      /**
       * {@inheritDoc}
       */
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
    if (getConnectedULCComponent().getDataType() != null) {
      return getConnectedULCComponent().getValue();
    }
    return super.getConnecteeValue();
  }

  /**
   * Sets the value to the connector text after formatting the string
   * representation.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (getConnectedULCComponent().getDataType() != null) {
      getConnectedULCComponent().setValue(aValue);
    } else {
      super.setConnecteeValue(aValue);
    }
  }
}
