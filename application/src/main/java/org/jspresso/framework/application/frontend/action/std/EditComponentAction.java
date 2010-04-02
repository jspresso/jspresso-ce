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
 * A simple action to edit a component in a form view. This action must be
 * followed by a view technology dependent action that will take care of
 * displaying the view in a dialog.
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
  // private IModelConnectorFactory modelConnectorFactory;
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

    // IValueConnector componentConnector = modelConnectorFactory
    // .createModelConnector(ACTION_MODEL_NAME, getViewDescriptor(context)
    // .getModelDescriptor(), actionHandler.getSubject());
    IValueConnector componentConnector = getBackendController(context)
        .createModelConnector(ACTION_MODEL_NAME,
            getViewDescriptor(context).getModelDescriptor());
    componentConnector.setConnectorValue(component);

    getMvcBinder(context)
        .bind(componentView.getConnector(), componentConnector);

    return super.execute(actionHandler, context);
  }

  /**
   * Sets the cancelAction.
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
   * Sets the okAction.
   * 
   * @param okAction
   *          the okAction to set.
   */
  public void setOkAction(IDisplayableAction okAction) {
    this.okAction = okAction;
  }

  /**
   * Sets the viewDescriptor.
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
