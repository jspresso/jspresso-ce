/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.wings;

import org.apache.commons.lang.StringUtils;
import org.jspresso.framework.binding.ConnectorValueChangeEvent;
import org.jspresso.framework.binding.IConnectorValueChangeListener;
import org.jspresso.framework.gui.wings.components.SActionField;
import org.wings.event.SDocumentEvent;
import org.wings.event.SDocumentListener;


/**
 * SActionFieldConnector connector.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
   *            the id of the connector.
   * @param actionField
   *            the connected SActionField.
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
  public void updateState() {
    super.updateState();
    getConnectedSComponent().setEditable(isWritable());
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
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    getConnectedSComponent().setValue(aValue);
  }
}
