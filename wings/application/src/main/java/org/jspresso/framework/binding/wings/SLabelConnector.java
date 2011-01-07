/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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

import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.html.HtmlHelper;
import org.wings.SLabel;

/**
 * A connector for label.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SLabelConnector extends SComponentConnector<SLabel> {

  private boolean    forceHtml;
  private IFormatter formatter;

  /**
   * Constructs a new <code>SLabelConnector</code> instance.
   * 
   * @param id
   *          the connector id.
   * @param connectedSComponent
   *          the label.
   */
  public SLabelConnector(String id, SLabel connectedSComponent) {
    super(id, connectedSComponent);
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
  public void setFormatter(IFormatter formatter) {
    this.formatter = formatter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindSComponent() {
    // Empty since a label is read-only.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    String text = getConnectedSComponent().getText();
    if (StringUtils.isEmpty(text)) {
      return null;
    }
    if (formatter != null) {
      try {
        Object value = formatter.parse(getConnectedSComponent().getText());
        getConnectedSComponent().setText(formatter.format(value));
        return value;
      } catch (ParseException ex) {
        setConnecteeValue(null);
        return null;
      }
    }
    return text;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (aValue == null) {
      getConnectedSComponent().setText(null);
    } else {
      if (formatter != null) {
        getConnectedSComponent().setText(formatter.format(aValue));
      } else if (forceHtml) {
        if (aValue.toString().toUpperCase().indexOf(HtmlHelper.HTML_START) < 0) {
          getConnectedSComponent().setText(
              HtmlHelper.toHtml(HtmlHelper.preformat(aValue.toString())));
        } else {
          getConnectedSComponent().setText(aValue.toString());
        }
      } else {
        getConnectedSComponent().setText(aValue.toString());
      }
    }
  }
}
