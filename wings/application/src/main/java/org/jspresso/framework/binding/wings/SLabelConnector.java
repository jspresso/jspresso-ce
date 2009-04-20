/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.binding.wings;

import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.html.HtmlHelper;
import org.wings.SLabel;

/**
 * A connector for label.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SLabelConnector extends SComponentConnector<SLabel> {

  private boolean multiLine;
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
  protected void setConnecteeValue(Object aValue) {
    if (aValue == null) {
      getConnectedSComponent().setText(null);
    } else {
      if (formatter != null) {
        getConnectedSComponent().setText(formatter.format(aValue));
      } else if (multiLine) {
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
