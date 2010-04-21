/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.binding.wings;

import org.apache.commons.lang.StringUtils;
import org.jspresso.framework.gui.wings.components.SActionField;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.wings.event.SDocumentEvent;
import org.wings.event.SDocumentListener;

/**
 * SActionFieldConnector connector.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SActionFieldConnector extends SComponentConnector<SActionField> {

  private Object value;

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
      addValueChangeListener(new IValueChangeListener() {

        public void valueChange(ValueChangeEvent evt) {
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
  public void writabilityChange() {
    super.writabilityChange();
    getConnectedSComponent().setEditable(isWritable());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindSComponent() {

    getConnectedSComponent().addTextFieldDocumentListener(
        new SDocumentListener() {

          public void changedUpdate(@SuppressWarnings("unused") SDocumentEvent e) {
            performActionIfNeeded();
          }

          public void insertUpdate(@SuppressWarnings("unused") SDocumentEvent e) {
            performActionIfNeeded();
          }

          public void removeUpdate(@SuppressWarnings("unused") SDocumentEvent e) {
            performActionIfNeeded();
          }
        });
  }

  /**
   * Gest the action text to display in the action field.
   * 
   * @return the action text to display in the action field.
   */
  protected String getActionText() {
    if (value == null) {
      return "";
    }
    return value.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    value = aValue;
    getConnectedSComponent().setValue(getActionText());
  }
}
