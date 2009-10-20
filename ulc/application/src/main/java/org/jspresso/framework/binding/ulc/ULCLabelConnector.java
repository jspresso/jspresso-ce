/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.html.HtmlHelper;

import com.ulcjava.base.application.ULCLabel;

/**
 * A connector for label.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCLabelConnector extends ULCComponentConnector<ULCLabel> {

  private boolean    multiLine;
  private IFormatter formatter;

  /**
   * Constructs a new <code>ULCLabelConnector</code> instance.
   * 
   * @param id
   *          the connector id.
   * @param connectedULCComponent
   *          the label.
   */
  public ULCLabelConnector(String id, ULCLabel connectedULCComponent) {
    super(id, connectedULCComponent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bindULCComponent() {
    // Empty since a label is read-only.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    if (aValue == null) {
      getConnectedULCComponent().setText(null);
    } else {
      if (formatter != null) {
        getConnectedULCComponent().setText(formatter.format(aValue));
      } else if (multiLine) {
        if (aValue.toString().toUpperCase().indexOf(HtmlHelper.HTML_START) < 0) {
          getConnectedULCComponent().setText(
              HtmlHelper.toHtml(HtmlHelper.preformat(aValue.toString())));
        } else {
          getConnectedULCComponent().setText(aValue.toString());
        }
      } else {
        getConnectedULCComponent().setText(aValue.toString());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    String text = getConnectedULCComponent().getText();
    if (StringUtils.isEmpty(text)) {
      return null;
    }
    if (formatter != null) {
      try {
        Object value = formatter.parse(getConnectedULCComponent().getText());
        getConnectedULCComponent().setText(formatter.format(value));
        return value;
      } catch (ParseException ex) {
        setConnecteeValue(null);
        return null;
      }
    }
    return text;
  }

  /**
   * Sets the multiLine.
   * 
   * @param multiLine
   *          the multiLine to set.
   */
  public void setMultiLine(boolean multiLine) {
    this.multiLine = multiLine;
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
}
