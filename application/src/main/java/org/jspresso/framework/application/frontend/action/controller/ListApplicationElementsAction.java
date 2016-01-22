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
 * @author Vincent Vandenschrick

 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ListApplicationElementsAction<E, F, G> extends FrontendAction<E, F, G> {

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
