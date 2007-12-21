/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.file;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.ulc.flow.InfoAction;

/**
 * A frontend action to display all usefull informations about a binary
 * property.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BinaryPropertyInfoAction extends InfoAction {

  /**
   * Displays the size of the binary property.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    Integer size = new Integer(0);
    String unit = "Ko";
    byte[] content = getBinaryContent(context);
    if (content != null) {
      if (content.length < 1024) {
        size = new Integer(content.length);
        unit = "o";
      } else if (content.length >= 1024 && content.length < 1024 * 1024) {
        size = new Integer(content.length / 1024);
        unit = "Ko";
      } else if (content.length >= 1024 * 1024
          && content.length < 1024 * 1024 * 1024) {
        size = new Integer(content.length / (1024 * 1024));
        unit = "Mo";
      } else if (content.length >= 1024 * 1024 * 1024) {
        size = new Integer(content.length / (1024 * 1024 * 1024));
        unit = "Go";
      }
    }
    context.put(ActionContextConstants.ACTION_PARAM, getTranslationProvider(
        context).getTranslation("binary.info.message",
        new Object[] {size, unit}, getLocale(context)));
    return super.execute(actionHandler, context);
  }

  /**
   * Retrieve the binary content to display the infos on.
   * 
   * @param context
   *            the action context.
   * @return The binary content to display the infos on or null.
   */
  protected byte[] getBinaryContent(Map<String, Object> context) {
    Object connectorValue = getViewConnector(context).getConnectorValue();
    byte[] content;
    if (connectorValue instanceof String) {
      content = ((String) connectorValue).getBytes();
    } else {
      content = (byte[]) connectorValue;
    }
    return content;
  }
}
