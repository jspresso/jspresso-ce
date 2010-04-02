/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.action.lov;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.action.ModalDialogAction;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.basic.BasicTableViewDescriptor;

/**
 * This action takes an arbitrary model collection connector from the action
 * context parameter and binds it to a newly created table view. This action is
 * meant to be chained to the generic <code>ModalDialogAction</code> so that the
 * table is actually poped-up in a dialog. Two actions (<code>okAction</code>
 * and <code>cancelAction</code>) can be configured to react to the user
 * decision.
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
public class ChooseComponentAction<E, F, G> extends FrontendAction<E, F, G> {

  private IDisplayableAction cancelAction;
  private IDisplayableAction okAction;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    List<IDisplayableAction> actions = new ArrayList<IDisplayableAction>();

    actions.add(okAction);
    actions.add(cancelAction);
    context.put(ModalDialogAction.DIALOG_ACTIONS, actions);

    IValueConnector componentsModelConnector = (IValueConnector) getActionParameter(context);
    BasicTableViewDescriptor tableViewDescriptor = new BasicTableViewDescriptor();
    tableViewDescriptor.setModelDescriptor(componentsModelConnector
        .getModelDescriptor());

    IView<E> collectionView = getViewFactory(context).createView(
        tableViewDescriptor, actionHandler, getLocale(context));
    String dialogTitle = getI18nName(getTranslationProvider(context),
        getLocale(context));
    if (dialogTitle != null && dialogTitle.length() > 0) {
      context.put(ModalDialogAction.DIALOG_TITLE, dialogTitle);
    }
    context.put(ModalDialogAction.DIALOG_VIEW, collectionView);

    getMvcBinder(context).bind(collectionView.getConnector(),
        componentsModelConnector);

    return super.execute(actionHandler, context);
  }

  /**
   * Configures the action that will be triggered when the user cancels the
   * component choice.
   * 
   * @param cancelAction
   *          the cancelAction to set.
   */
  public void setCancelAction(IDisplayableAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Configures the action that will be triggered when the user confirms the
   * component choice. the chosen component will then be retrieved from the
   * selected view item.
   * 
   * @param okAction
   *          the okAction to set.
   */
  public void setOkAction(IDisplayableAction okAction) {
    this.okAction = okAction;
  }
}
