/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.wings.std;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.wings.SComponent;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.model.IModelConnectorFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IDisplayableAction;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * A simple action to edit a component in a form view.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EditComponentAction extends ModalDialogAction {

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

    IView<SComponent> componentView = getViewFactory(context).createView(
        getViewDescriptor(context), actionHandler, getLocale(context));
    context.put(ActionContextConstants.DIALOG_VIEW, componentView);

    IValueConnector componentConnector = modelConnectorFactory
        .createModelConnector(ACTION_MODEL_NAME, getViewDescriptor(context)
            .getModelDescriptor());
    componentConnector.setConnectorValue(getModel(context));

    getMvcBinder(context)
        .bind(componentView.getConnector(), componentConnector);

    return super.execute(actionHandler, context);
  }

  /**
   * Sets the cancelAction.
   * 
   * @param cancelAction
   *            the cancelAction to set.
   */
  public void setCancelAction(IDisplayableAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Sets the modelConnectorFactory.
   * 
   * @param modelConnectorFactory
   *            the modelConnectorFactory to set.
   */
  public void setModelConnectorFactory(
      IModelConnectorFactory modelConnectorFactory) {
    this.modelConnectorFactory = modelConnectorFactory;
  }

  /**
   * Sets the okAction.
   * 
   * @param okAction
   *            the okAction to set.
   */
  public void setOkAction(IDisplayableAction okAction) {
    this.okAction = okAction;
  }

  /**
   * Sets the viewDescriptor.
   * 
   * @param viewDescriptor
   *            the viewDescriptor to set.
   */
  public void setViewDescriptor(IViewDescriptor viewDescriptor) {
    this.viewDescriptor = viewDescriptor;
  }

  /**
   * Gets the model.
   * 
   * @param context
   *            the action context.
   * @return the model.
   */
  protected Object getModel(Map<String, Object> context) {
    return context.get(ActionContextConstants.ACTION_PARAM);
  }

  /**
   * Gets the viewDescriptor.
   * 
   * @param context
   *            the action context.
   * @return the viewDescriptor.
   */
  protected IViewDescriptor getViewDescriptor(@SuppressWarnings("unused")
  Map<String, Object> context) {
    return viewDescriptor;
  }
}
