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
package org.jspresso.framework.application.frontend.action.std;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.frontend.action.ModalDialogAction;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This action pulls a model out of the context (action parameter), creates a
 * view, binds it on the model and prepares for chaining with a modal dialog
 * action to pop-up the result. The translated name of the action, whenever not
 * empty, will be used as the dialog title. If the context extracted model is a
 * collection, the first element of the collection is used. Custom actions (
 * <code>okAction</code> and <code>cancelAction</code>) can be configured to
 * take care of user decision when closing the dialog.
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
public class EditComponentAction<E, F, G> extends FrontendAction<E, F, G> {

  private IDisplayableAction cancelAction;
  private IDisplayableAction okAction;
  private IViewDescriptor    viewDescriptor;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    List<IDisplayableAction> actions = new ArrayList<IDisplayableAction>();

    Object component = getComponentToEdit(context);

    if (okAction != null) {
      actions.add(okAction);
    }
    if (cancelAction != null) {
      actions.add(cancelAction);
    }
    context.put(ModalDialogAction.DIALOG_ACTIONS, actions);

    IView<E> componentView = getViewFactory(context).createView(
        getViewDescriptor(context), actionHandler, getLocale(context));
    String dialogTitle = getI18nName(getTranslationProvider(context),
        getLocale(context));
    if (dialogTitle != null && dialogTitle.length() > 0) {
      context.put(ModalDialogAction.DIALOG_TITLE, dialogTitle);
    }
    context.put(ModalDialogAction.DIALOG_VIEW, componentView);

    IValueConnector componentConnector = getBackendController(context)
        .createModelConnector(ACTION_MODEL_NAME,
            getViewDescriptor(context).getModelDescriptor());
    componentConnector.setConnectorValue(component);

    getMvcBinder(context)
        .bind(componentView.getConnector(), componentConnector);

    return super.execute(actionHandler, context);
  }

  /**
   * Configures the action to be installed in the dialog when the user cancels
   * the component edition.
   * 
   * @param cancelAction
   *          the cancelAction to set.
   */
  public void setCancelAction(IDisplayableAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Sets the modelConnectorFactory.
   * 
   * @param modelConnectorFactory
   *          the modelConnectorFactory to set.
   * @deprecated modeconnector is now created by the backend controller.
   */
  @Deprecated
  public void setModelConnectorFactory(
      IModelConnectorFactory modelConnectorFactory) {
    // this.modelConnectorFactory = modelConnectorFactory;
  }

  /**
   * Configures the action to be installed in the dialog when the user confirms
   * the component edition.
   * 
   * @param okAction
   *          the okAction to set.
   */
  public void setOkAction(IDisplayableAction okAction) {
    this.okAction = okAction;
  }

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
  protected Object getComponentToEdit(Map<String, Object> context) {
    Object model = getActionParameter(context);
    if (model instanceof Collection<?>) {
      if (((Collection<?>) model).isEmpty()) {
        return null;
      }
      return ((Collection<?>) model).iterator().next();
    }
    return model;
  }

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
}
