/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * A generic modal dialog action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ModalDialogAction<E, F, G> extends WrappingAction<E, F, G> {

  /**
   * Shows a modal dialog containig a main view and a button panel with the list
   * of registered actions.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    IView<E> mainView = getMainView(context);
    List<IDisplayableAction> dActions = getActions(context);
    String title = getI18nName(getTranslationProvider(context),
        getLocale(context));
    E sourceComponent = getSourceComponent(context);

    List<G> actions = new ArrayList<G>();
    for (IDisplayableAction action : dActions) {
      actions.add(getActionFactory(context).createAction(action, actionHandler,
          mainView, getLocale(context)));
    }
    getController(context).displayModalDialog(mainView.getPeer(), actions,
        title, sourceComponent, context, false);
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the actions.
   * 
   * @param context
   *          the action context.
   * @return the actions.
   */
  @SuppressWarnings("unchecked")
  public List<IDisplayableAction> getActions(Map<String, Object> context) {
    return (List<IDisplayableAction>) context
        .get(ActionContextConstants.DIALOG_ACTIONS);
  }

  /**
   * Gets the mainView.
   * 
   * @param context
   *          the action context.
   * @return the mainView.
   */
  @SuppressWarnings("unchecked")
  public IView<E> getMainView(Map<String, Object> context) {
    return (IView<E>) context.get(ActionContextConstants.DIALOG_VIEW);
  }

}
