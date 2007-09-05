/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.wings;

import org.apache.commons.lang.StringUtils;
import org.wings.event.SDocumentEvent;
import org.wings.event.SDocumentListener;

import com.d2s.framework.binding.ConnectorValueChangeEvent;
import com.d2s.framework.binding.IConnectorValueChangeListener;
import com.d2s.framework.gui.wings.components.SActionField;

/**
 * SActionFieldConnector connector.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SActionFieldConnector extends SComponentConnector<SActionField> {

  /**
   * Constructs a new <code>SActionFieldConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param actionField
   *          the connected SActionField.
   */
  public SActionFieldConnector(String id, SActionField actionField) {
    super(id, actionField);
    if (!getConnectedSComponent().isShowingTextField()) {
      addConnectorValueChangeListener(new IConnectorValueChangeListener() {

        public void connectorValueChange(ConnectorValueChangeEvent evt) {
          if (evt.getNewValue() instanceof byte[]) {
            getConnectedSComponent().setDecorated(
                ((byte[]) evt.getNewValue()).length > 0);
          } else {
            getConnectedSComponent().setDecorated(evt.getNewValue() != null);
          }
        }
      });
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindSComponent() {

    getConnectedSComponent().addTextFieldDocumentListener(
        new SDocumentListener() {

          public void changedUpdate(@SuppressWarnings("unused")
          SDocumentEvent e) {
            performActionIfNeeded();
          }

          public void insertUpdate(@SuppressWarnings("unused")
          SDocumentEvent e) {
            performActionIfNeeded();
          }

          public void removeUpdate(@SuppressWarnings("unused")
          SDocumentEvent e) {
            performActionIfNeeded();
          }
        });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getConnectedSComponent().getValue();
  }

  /**
   * Performs the action field action if the action field is not synchronized.
   */
  public void performActionIfNeeded() {
    if (!getConnectedSComponent().isSynchronized()) {
      if (StringUtils.isEmpty(getConnectedSComponent().getActionText())) {
        setConnectorValue(null);
      } else {
        getConnectedSComponent().performAction();
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    getConnectedSComponent().setValue(aValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateState() {
    super.updateState();
    getConnectedSComponent().setEditable(isWritable());
  }
}
