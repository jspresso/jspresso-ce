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
package org.jspresso.framework.application.frontend.action.std;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.AbstractChainedAction;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * A simple action to edit a component in a form view. This action must be
 * followed by a view technology dependent action that will take care of
 * displaying the view in a dialog.
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
public class EditComponentAction<E, F, G> extends
    AbstractChainedAction<E, F, G> {

  private IDisplayableAction     cancelAction;
  private IModelConnectorFactory modelConnectorFactory;
  private IDisplayableAction     okAction;
  private IViewDescriptor        viewDescriptor;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    List<IDisplayableAction> actions = new ArrayList<IDisplayableAction>();

    Object component = context.get(ActionContextConstants.ACTION_PARAM);

    okAction.putInitialContext(ActionContextConstants.SOURCE_VIEW_CONNECTOR,
        getViewConnector(context));
    okAction.putInitialContext(ActionContextConstants.ACTION_PARAM, component);
    actions.add(okAction);
    actions.add(cancelAction);
    context.put(ActionContextConstants.DIALOG_ACTIONS, actions);

    IView<E> componentView = getViewFactory(context).createView(
        getViewDescriptor(context), actionHandler, getLocale(context));
    context.put(ActionContextConstants.DIALOG_VIEW, componentView);

    IValueConnector componentConnector = modelConnectorFactory
        .createModelConnector(ACTION_MODEL_NAME, getViewDescriptor(context)
            .getModelDescriptor());
    componentConnector.setConnectorValue(getModel(context));

    getMvcBinder(context)
        .bind(componentView.getConnector(), componentConnector);

    return true;
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
   */
  public void setModelConnectorFactory(
      IModelConnectorFactory modelConnectorFactory) {
    this.modelConnectorFactory = modelConnectorFactory;
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
  protected Object getModel(Map<String, Object> context) {
    return context.get(ActionContextConstants.ACTION_PARAM);
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
