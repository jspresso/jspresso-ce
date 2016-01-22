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

import java.util.Collection;
import java.util.Map;

import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * This action pulls a model out of the context (action parameter or selected
 * model if action parameter is not filled), creates a view, binds it on the
 * model and prepares for chaining with a modal dialog action to pop-up the
 * result. The translated name of the action, whenever not empty, will be used
 * as the dialog title. If the context extracted model is a collection, the
 * first element of the collection is used. Custom actions (
 * {@code okAction} and {@code cancelAction}) can be configured to
 * take care of user decision when closing the dialog.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class EditComponentAction<E, F, G> extends
    AbstractEditComponentAction<E, F, G> {

  private IDisplayableAction cancelAction;
  private IDisplayableAction okAction;

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
   * Gets the cancelAction.
   *
   * @return the cancelAction.
   */
  @Override
  protected IDisplayableAction getCancelAction() {
    return cancelAction;
  }

  /**
   * Sets the modelConnectorFactory.
   *
   * @param modelConnectorFactory
   *          the modelConnectorFactory to set.
   * @deprecated model connector is now created by the backend controller.
   */
  @SuppressWarnings({"EmptyMethod", "UnusedParameters"})
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
   * Gets the okAction.
   *
   * @return the okAction.
   */
  @Override
  protected IDisplayableAction getOkAction() {
    return okAction;
  }

  /**
   * Gets the model.
   *
   * @param context
   *          the action context.
   * @return the model.
   */
  @Override
  protected Object getComponentToEdit(Map<String, Object> context) {
    Object model = getActionParameter(context);
    if (model == null) {
      model = getSelectedModel(context);
    }
    if (model instanceof Collection<?>) {
      if (((Collection<?>) model).isEmpty()) {
        return null;
      }
      return ((Collection<?>) model).iterator().next();
    }
    return model;
  }
}
