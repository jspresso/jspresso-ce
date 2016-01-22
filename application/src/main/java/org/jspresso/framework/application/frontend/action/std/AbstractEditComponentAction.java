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
package org.jspresso.framework.application.frontend.action.std;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.action.ModalDialogAction;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This action serves as a base class for actions that pop-pup a dialog to edit
 * a component.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public abstract class AbstractEditComponentAction<E, F, G> extends
    FrontendAction<E, F, G> {

  private IViewDescriptor                    viewDescriptor;
  private List<? extends IDisplayableAction> complementaryActions;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    List<IDisplayableAction> actions = getDialogActions();
    context.put(ModalDialogAction.DIALOG_ACTIONS, actions);

    IViewDescriptor editViewDescriptor = getViewDescriptor(context);

    IView<E> dialogView = getViewFactory(context).createView(
        editViewDescriptor, actionHandler, getLocale(context));
    String dialogTitle = getI18nName(getTranslationProvider(context),
        getLocale(context));
    if (dialogTitle != null && dialogTitle.length() > 0) {
      context.put(ModalDialogAction.DIALOG_TITLE, dialogTitle);
    }
    context.put(ModalDialogAction.DIALOG_VIEW, dialogView);

    Object component = getComponentToEdit(context);
    IModelDescriptor modelDescriptor = getEditModelDescriptor(context);
    if (modelDescriptor == null
        && editViewDescriptor instanceof ICardViewDescriptor) {
      ICardViewDescriptor cvd = (ICardViewDescriptor) editViewDescriptor;
      String cardName = cvd.getCardNameForModel(component,
          getBackendController(context).getApplicationSession().getSubject());
      IViewDescriptor vd = cvd.getCardViewDescriptor(cardName);
      modelDescriptor = vd.getModelDescriptor();
    }

    IValueConnector componentConnector = getBackendController(context)
        .createModelConnector(ACTION_MODEL_NAME, modelDescriptor);
    componentConnector.setConnectorValue(component);

    getMvcBinder(context)
        .bind(dialogView.getConnector(), componentConnector);

    return super.execute(actionHandler, context);
  }

  /**
   * Constructs the list of actions that will be installed on the dialog toolbar.
   *
   * @return the list of actions that will be installed on the dialog toolbar.
   */
  protected List<IDisplayableAction> getDialogActions() {
    List<IDisplayableAction> actions = new ArrayList<>();
    if (getOkAction() != null) {
      actions.add(getOkAction());
    }
    if (getComplementaryActions() != null) {
      actions.addAll(getComplementaryActions());
    }
    if (getCancelAction() != null) {
      actions.add(getCancelAction());
    }
    return actions;
  }

  /**
   * Gets the okAction.
   *
   * @return the okAction.
   */
  protected abstract IDisplayableAction getOkAction();

  /**
   * Gets the cancelAction.
   *
   * @return the cancelAction.
   */
  protected abstract IDisplayableAction getCancelAction();

  /**
   * Configures the view descriptor to be used to create the component editing
   * view that will be installed in the dialog.
   *
   * @param viewDescriptor
   *          the viewDescriptor to set.
   */
  public void setViewDescriptor(IViewDescriptor viewDescriptor) {
    this.viewDescriptor = viewDescriptor;
  }

  /**
   * Gets the model.
   *
   * @param context
   *          the action context.
   * @return the model.
   */
  protected abstract Object getComponentToEdit(Map<String, Object> context);

  /**
   * Gets the viewDescriptor.
   *
   * @param context
   *          the action context.
   * @return the viewDescriptor.
   */
  protected IViewDescriptor getViewDescriptor(Map<String, Object> context) {
    return viewDescriptor;
  }

  /**
   * Gets the modelDescriptor.
   *
   * @param context
   *          the action context.
   * @return the modelDescriptor.
   */
  protected IModelDescriptor getEditModelDescriptor(Map<String, Object> context) {
    return getViewDescriptor(context).getModelDescriptor();
  }

  /**
   * Gets the complementaryActions.
   *
   * @return the complementaryActions.
   */
  public List<? extends IDisplayableAction> getComplementaryActions() {
    return complementaryActions;
  }

  /**
   * Installs a list of complementary actions to install between the ok and
   * cancel actions.
   *
   * @param complementaryActions
   *          the complementaryActions to set.
   */
  public void setComplementaryActions(
      List<? extends IDisplayableAction> complementaryActions) {
    this.complementaryActions = complementaryActions;
  }

}
