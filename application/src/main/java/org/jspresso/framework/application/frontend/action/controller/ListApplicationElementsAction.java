/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.controller;

import java.util.Map;
import java.util.Set;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.security.ApplicationDirectoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a special frontend action to list all application metamodel elements
 * available along with their permIds. This is particularly useful to set-up the
 * base of security referential setup.
 * 
 * @version $LastChangedRevision: 3701 $
 * @author Vincent Vandenschrick
 */
public class ListApplicationElementsAction extends FrontendAction {

  private static final Logger LOG = LoggerFactory.getLogger(ListApplicationElementsAction.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    ApplicationDirectoryBuilder directoryBuilder = new ApplicationDirectoryBuilder();
    directoryBuilder.process(getController(context));
    Map<String, Set<String>> permIdsStore = directoryBuilder
        .toApplicationDirectory();
    for (Map.Entry<String, Set<String>> storeEntry : permIdsStore.entrySet()) {
      LOG.info("\n\n********************************************");
      LOG.info("************ " + storeEntry.getKey() + " *****************");
      LOG.info("********************************************");
      for (String permId : storeEntry.getValue()) {
        LOG.info(permId);
      }
    }
    return super.execute(actionHandler, context);
  }

}
