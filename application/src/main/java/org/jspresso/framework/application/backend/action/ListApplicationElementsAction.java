/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action;

import java.util.Map;
import java.util.Set;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.application.security.ApplicationDirectoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * This is a special frontend action to list all application metamodel elements
 * available along with their permIds. This is particularly useful to set-up the
 * base of security referential setup.
 * 
 * @version $LastChangedRevision: 3701 $
 * @author Vincent Vandenschrick
 */
public class ListApplicationElementsAction extends BackendAction implements
    ApplicationContextAware {

  private static final Logger LOG = LoggerFactory.getLogger(ListApplicationElementsAction.class);
  private ApplicationContext applicationContext;

  /**
   * cofigures the application context this action was instanciated from.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    ApplicationDirectoryBuilder directoryBuilder = new ApplicationDirectoryBuilder();
    Map<String, Workspace> workspaces = applicationContext
        .getBeansOfType(Workspace.class);
    for (Workspace workspace : workspaces.values()) {
      directoryBuilder.process(workspace);
    }
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
