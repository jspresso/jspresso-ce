/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.echo;

import nextapp.echo2.app.SelectField;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

/**
 * ComboBox connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SelectFieldConnector extends ComponentConnector<SelectField> {

  /**
   * Constructs a new <code>SelectFieldConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param comboBox
   *          the connected SelectField.
   */
  public SelectFieldConnector(String id, SelectField comboBox) {
    super(id, comboBox);
  }

  /**
   * Returns the selected object in the combobox.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getConnectedComponent().getSelectedItem();
  }

  /**
   * Sets the selected item in the combobox to be the connector value to set.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    for (int i = 0; i < getConnectedComponent().getModel().size(); i++) {
      if (aValue.equals(getConnectedComponent().getModel().get(i))) {
        getConnectedComponent().setSelectedIndex(i);
        break;
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindComponent() {
    getConnectedComponent().addActionListener(new ActionListener() {

      private static final long serialVersionUID = 2920422344648297005L;

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
   * {@inheritDoc}
   */
  @Override
  public void updateState() {
    super.updateState();
    getConnectedComponent().setEnabled(isWritable());
  }
}
