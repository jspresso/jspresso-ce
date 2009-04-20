/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.swing;

import java.text.ParseException;

import javax.swing.JLabel;

import org.apache.commons.lang.StringUtils;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.html.HtmlHelper;

/**
 * A connector for label.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class JLabelConnector extends JComponentConnector<JLabel> {

  private boolean    multiLine;
  private IFormatter formatter;

  /**
   * Constructs a new <code>JLabelConnector</code> instance.
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
  protected void protectedSetConnecteeValue(Object aValue) {
    if (aValue == null) {
      getConnectedJComponent().setText(null);
    } else {
      if (formatter != null) {
        getConnectedJComponent().setText(formatter.format(aValue));
      } else if (multiLine) {
        if (aValue.toString().toUpperCase().indexOf(HtmlHelper.HTML_START) < 0) {
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    String text = getConnectedJComponent().getText();
    if (StringUtils.isEmpty(text)) {
      return null;
    }
    if (formatter != null) {
      try {
        Object value = formatter.parse(getConnectedJComponent().getText());
        getConnectedJComponent().setText(formatter.format(value));
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
