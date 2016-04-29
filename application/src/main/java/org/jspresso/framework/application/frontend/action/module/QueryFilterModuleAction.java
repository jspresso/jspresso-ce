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
package org.jspresso.framework.application.frontend.action.module;

import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.application.model.Module;

/**
 * Queries filter module and notify user of empty record set.
 *
 * @param <E>
 *     the actual gui component type used.
 * @param <F>
 *     the actual icon type used.
 * @param <G>
 *     the actual action type used.
 * @author Vincent Vandenschrick
 */
public class QueryFilterModuleAction<E, F, G> extends FrontendAction<E,F,G> {

  private IAction emptyResultAction;

  /**
   * Execute boolean.
   *
   * @param actionHandler
   *     the action handler
   * @param context
   *     the context
   * @return the boolean
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    boolean success = super.execute(actionHandler, context);
    final Module module = getModule(context);
    if (module instanceof FilterableBeanCollectionModule) {
      if (((FilterableBeanCollectionModule) module).getModuleObjects() == null
          || ((FilterableBeanCollectionModule) module).getModuleObjects().isEmpty()) {
        actionHandler.execute(emptyResultAction, context);
      }
    }
    return success;
  }

  /**
   * Gets empty result action.
   *
   * @return the empty result action
   */
  protected IAction getEmptyResultAction() {
    return emptyResultAction;
  }

  /**
   * Sets empty result action.
   *
   * @param emptyResultAction
   *     the empty result action
   */
  public void setEmptyResultAction(IAction emptyResultAction) {
    this.emptyResultAction = emptyResultAction;
  }
}
