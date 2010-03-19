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
package org.jspresso.framework.binding.ulc;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.commons.lang.StringUtils;
import org.jspresso.framework.gui.ulc.components.server.ULCActionField;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;

/**
 * ULCActionFieldConnector connector.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCActionFieldConnector extends
    ULCComponentConnector<ULCActionField> {

  private Object value;

  /**
   * Constructs a new <code>ULCActionFieldConnector</code> instance.
   * 
   * @param id
   *          the id of the connector.
   * @param actionField
   *          the connected ULCActionField.
   */
  public ULCActionFieldConnector(String id, ULCActionField actionField) {
    super(id, actionField);
    if (!getConnectedULCComponent().isShowingTextField()) {
      addValueChangeListener(new IValueChangeListener() {

        public void valueChange(ValueChangeEvent evt) {
          if (evt.getNewValue() instanceof byte[]) {
            getConnectedULCComponent().setDecorated(
                ((byte[]) evt.getNewValue()).length > 0);
          } else {
            getConnectedULCComponent().setDecorated(evt.getNewValue() != null);
          }
        }
      });
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writabilityChange() {
    super.writabilityChange();
    getConnectedULCComponent().setEditable(isWritable());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindULCComponent() {
    getConnectedULCComponent().addPropertyChangeListener("actionText",
        new PropertyChangeListener() {

          /**
           * {@inheritDoc}
           */
          public void propertyChange(
              @SuppressWarnings("unused") PropertyChangeEvent evt) {
            performActionIfNeeded();
          }
        });

    getConnectedULCComponent().addPropertyChangeListener("value",
        new PropertyChangeListener() {

          /**
           * {@inheritDoc}
           */
          public void propertyChange(
              @SuppressWarnings("unused") PropertyChangeEvent evt) {
            fireConnectorValueChange();
          }
        });
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
    getConnectedULCComponent().setValue(getActionText());
  }

  /**
   * Performs the action field action if the action field is not synchronized.
   */
  private void performActionIfNeeded() {
    if (!getConnectedULCComponent().isSynchronized()) {
      if (StringUtils.isEmpty(getConnectedULCComponent().getActionText())) {
        getConnectedULCComponent().setValue(null);
      } else {
        getConnectedULCComponent().performAction(0);
      }
    }
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
}
