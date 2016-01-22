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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * This is a very generic action that takes its specifications out of the action
 * context, creates and pops-up a modal dialog based on these specs. The action
 * is meant to be chained after another frontend action that produce the dialog
 * specs into the action context. Context entries that are used are :
 * <ul>
 * <li>{@code DIALOG_VIEW} : the view to be used as the dialog content
 * pane.</li>
 * <li>{@code DIALOG_TITLE} : the title of the dialog.</li>
 * <li>{@code DIALOG_ACTIONS} : the actions to be installed at the bottom
 * of the dialog.</li>
 * <li>{@code DIALOG_SIZE} : the dialog preferred size. Whenever the dialog
 * size is {@code null}, the dialog size is determined from the preferred
 * size of the content view.</li>
 * </ul>
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ModalDialogAction<E, F, G> extends FrontendAction<E, F, G> {

  /**
   * The dialog actions.
   */
  public static final String DIALOG_ACTIONS           = "DIALOG_ACTIONS";

  /**
   * The dialog size.
   */
  public static final String DIALOG_SIZE              = "DIALOG_SIZE";

  /**
   * The dialog title.
   */
  public static final String DIALOG_TITLE             = "DIALOG_TITLE";

  /**
   * The dialog view.
   */
  public static final String DIALOG_VIEW              = "DIALOG_VIEW";

  /**
   * The focused component.
   */
  public static final String DIALOG_FOCUSED_COMPONENT = "DIALOG_FOCUSED_COMPONENT";

  /**
   * Shows a modal dialog containing a main view and a button panel with the list
   * of registered actions.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    IView<E> mainView = getMainView(context);
    List<IDisplayableAction> dActions = getActions(context);
    String title = getTitle(context);
    if (title == null) {
      title = getI18nName(getTranslationProvider(context), getLocale(context));
    }
    E sourceComponent = getSourceComponent(context);

    List<G> actions = new ArrayList<>();
    for (IDisplayableAction action : dActions) {
      actions.add(getActionFactory(context).createAction(action, actionHandler,
          mainView, getLocale(context)));
    }
    getController(context).displayModalDialog(mainView.getPeer(), actions,
        title, sourceComponent, context, getDialogSize(context), false);
    E focusedComponent = getFocusedComponent(context);
    if (focusedComponent != null) {
      getController(context).focus(focusedComponent);
    }
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
    return (List<IDisplayableAction>) context.get(DIALOG_ACTIONS);
  }

  /**
   * Gets the dialog size.
   *
   * @param context
   *          the action context.
   * @return the dialog size.
   */
  public Dimension getDialogSize(Map<String, Object> context) {
    return (Dimension) context.get(DIALOG_SIZE);
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
    return (IView<E>) context.get(DIALOG_VIEW);
  }

  /**
   * Gets the title.
   *
   * @param context
   *          the action context.
   * @return the dialog title.
   */
  public String getTitle(Map<String, Object> context) {
    return (String) context.get(DIALOG_TITLE);
  }

  /**
   * Gets the focused component.
   *
   * @param context
   *          the action context.
   * @return the focused component or null if not set.
   */
  @SuppressWarnings("unchecked")
  public E getFocusedComponent(Map<String, Object> context) {
    return (E) context.get(DIALOG_FOCUSED_COMPONENT);
  }
}
