/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding.swing;

import javax.swing.JLabel;

import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.html.HtmlHelper;

/**
 * A connector for label.
 *
 * @author Vincent Vandenschrick
 */
public class JLabelConnector extends JComponentConnector<JLabel> {

  private boolean               forceHtml;
  private IFormatter<?, String> formatter;

  private Object                connecteeValue;

  /**
   * Constructs a new {@code JLabelConnector} instance.
   *
   * @param id
   *          the connector id.
   * @param connectedJComponent
   *          the label.
   */
  public JLabelConnector(String id, JLabel connectedJComponent) {
    super(id, connectedJComponent);
  }

  /**
   * Sets the forceHtml.
   *
   * @param forceHtml
   *          the forceHtml to set.
   */
  public void setForceHtml(boolean forceHtml) {
    this.forceHtml = forceHtml;
  }

  /**
   * Sets the formatter.
   *
   * @param formatter
   *          the formatter to set.
   */
  public void setFormatter(IFormatter<?, String> formatter) {
    this.formatter = formatter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindJComponent() {
    // Empty since a label is read-only.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return connecteeValue;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  protected void protectedSetConnecteeValue(Object aValue) {
    connecteeValue = aValue;
    if (aValue == null) {
      getConnectedJComponent().setText(null);
    } else {
      if (formatter != null) {
        getConnectedJComponent().setText(
            ((IFormatter<Object, String>) formatter).format(aValue));
      } else if (forceHtml) {
        if (!aValue.toString().toUpperCase().contains(HtmlHelper.HTML_START)) {
          getConnectedJComponent().setText(
              HtmlHelper.toHtml(HtmlHelper.preformat(aValue.toString())));
        } else {
          getConnectedJComponent().setText(aValue.toString());
        }
      } else {
        getConnectedJComponent().setText(aValue.toString());
      }
    }
  }
}
