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
package org.jspresso.framework.application.frontend.action;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;

/**
 * This is a very generic action that closes (disposes) the currently opened
 * dialog. The dialog is actually closed between the <i>wrapped</i> action and
 * the <i>next</i> action if and only if the wrapped action succeeds.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class CloseDialogAction<E, F, G> extends FrontendAction<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    // do not call super since dialog must be closed between wrapped and next
    // action.
    // return super.execute(actionHandler, context);
    if (actionHandler.execute(getWrappedAction(context), context)) {
      if (getController(context).disposeModalDialog(getActionWidget(context),
          context)) {
        if (getNextAction(context) != null) {
          return actionHandler.execute(getNextAction(context), context);
        }
        return true;
      }
    }
    return false;
  }
}
