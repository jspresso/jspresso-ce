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
package org.jspresso.framework.application.frontend.action.flow;

import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;

/**
 * This action pops-up an Ok - Cancel confirmation option. Depending on user
 * answer, another action is triggered. The Ok - Cancel alternative actions are
 * parametrized statically.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class OkCancelAction<E, F, G> extends AbstractMessageAction<E, F, G> {

  private IAction cancelAction;
  private IAction okAction;

  /**
   * Displays an ok cancel dialog
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    getController(context)
        .popupOkCancel(getSourceComponent(context),
            getI18nName(getTranslationProvider(context), getLocale(context)),
            getIconImageURL(), getMessage(context), okAction, cancelAction,
            context);
    return super.execute(actionHandler, context);
  }

  /**
   * Assigns the action to execute when the user cancels the option.
   *
   * @param cancelAction
   *          the cancelAction to set.
   */
  public void setCancelAction(IAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Assigns the action to execute when the user confirms the option.
   *
   * @param okAction
   *          the okAction to set.
   */
  public void setOkAction(IAction okAction) {
    this.okAction = okAction;
  }

}
