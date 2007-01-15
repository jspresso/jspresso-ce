/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.echo;

import gui.echo.components.ActionField;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

import org.apache.commons.lang.StringUtils;

import com.d2s.framework.binding.ConnectorValueChangeEvent;
import com.d2s.framework.binding.IConnectorValueChangeListener;

/**
 * ActionField connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ActionFieldConnector extends ComponentConnector<ActionField> {

  /**
   * Constructs a new <code>JActionFieldConnector</code> instance.
   *
   * @param id
   *          the id of the connector.
   * @param actionField
   *          the connected ActionField.
   */
  public ActionFieldConnector(String id, ActionField actionField) {
    super(id, actionField);
    if (!getConnectedComponent().isShowingTextField()) {
      addConnectorValueChangeListener(new IConnectorValueChangeListener() {

        public void connectorValueChange(ConnectorValueChangeEvent evt) {
          if (evt.getNewValue() instanceof byte[]) {
            getConnectedComponent().setDecorated(
                ((byte[]) evt.getNewValue()).length > 0);
          } else {
            getConnectedComponent().setDecorated(evt.getNewValue() != null);
          }
        }
      });
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindComponent() {
    getConnectedComponent().addTextFieldActionListener(new ActionListener() {

      private static final long serialVersionUID = 0L;

      public void actionPerformed(@SuppressWarnings("unused")
      ActionEvent e) {
        performActionIfNeeded();
      }
    });
  }

  /**
   * Performs the action field action if the action field is not synchronized.
   */
  public void performActionIfNeeded() {
    if (!getConnectedComponent().isSynchronized()) {
      if (StringUtils.isEmpty(getConnectedComponent().getActionText())) {
        setConnectorValue(null);
      } else {
        getConnectedComponent().performAction();
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    getConnectedComponent().setValue(aValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getConnectedComponent().getValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateState() {
    super.updateState();
    getConnectedComponent().setEditable(isWritable());
  }
}
