/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.file;

import java.util.Locale;
import java.util.Map;

import com.d2s.framework.application.frontend.action.ulc.flow.InfoAction;
import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * A frontend action to display all usefull informations about a binary
 * property.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
  protected String getI18nMessage(ITranslationProvider translationProvider,
      Locale locale, Map<String, Object> context) {
    Integer kbSize = new Integer(0);
    byte[] content = getBinaryContent(context);
    if (content != null) {
      kbSize = new Integer(content.length / 1024);
    }
    return translationProvider.getTranslation("binary.info.message",
        new Object[] {kbSize}, locale);
  }

  /**
   * Retrieve the binary content to display the infos on.
   * 
   * @param context
   *          the action context.
   * @return The binary content to display the infos on or null.
   */
  protected byte[] getBinaryContent(Map<String, Object> context) {
    byte[] content = (byte[]) getViewConnector(context).getConnectorValue();
    return content;
  }
}
