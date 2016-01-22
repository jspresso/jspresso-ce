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
 * This action pops-up a binary question. Depending on user answer, another
 * action is triggered. The Yes - No alternative actions are parametrized
 * statically.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class YesNoAction<E, F, G> extends AbstractMessageAction<E, F, G> {

  private IAction noAction;
  private IAction yesAction;

  /**
   * Displays the message using a {@code JOptionPane.YES_NO_OPTION}.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    getController(context).popupYesNo(getSourceComponent(context),
        getI18nName(getTranslationProvider(context), getLocale(context)),
        getIconImageURL(), getMessage(context), yesAction, noAction, context);
    return super.execute(actionHandler, context);
  }

  /**
   * Assigns the action to execute when the user answers negatively to the
   * question.
   *
   * @param noAction
   *          the noAction to set.
   */
  public void setNoAction(IAction noAction) {
    this.noAction = noAction;
  }

  /**
   * Assigns the action to execute when the user answers positively to the
   * question.
   *
   * @param yesAction
   *          the yesAction to set.
   */
  public void setYesAction(IAction yesAction) {
    this.yesAction = yesAction;
  }

}
