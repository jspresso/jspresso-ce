/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.lov;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.application.frontend.action.ulc.flow.ModalDialogAction;
import com.d2s.framework.binding.model.IModelValueConnector;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IDisplayableAction;
import com.d2s.framework.view.descriptor.basic.BasicTableViewDescriptor;
import com.ulcjava.base.application.ULCComponent;

/**
 * A standard List of value action for reference property views. This action
 * should be used in view factories.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ChooseComponentAction extends ModalDialogAction {

  private IDisplayableAction okAction;
  private IDisplayableAction cancelAction;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    List<IDisplayableAction> actions = new ArrayList<IDisplayableAction>();

    okAction.putInitialContext(ActionContextConstants.SOURCE_VIEW_CONNECTOR,
        getViewConnector(context));
    actions.add(okAction);
    actions.add(cancelAction);
    context.put(ActionContextConstants.DIALOG_ACTIONS, actions);

    IModelValueConnector componentsModelConnector = (IModelValueConnector) context
        .get(ActionContextConstants.ACTION_PARAM);
    BasicTableViewDescriptor tableViewDescriptor = new BasicTableViewDescriptor();
    tableViewDescriptor.setModelDescriptor(componentsModelConnector
        .getModelDescriptor());

    IView<ULCComponent> collectionView = getViewFactory(context).createView(
        tableViewDescriptor, actionHandler, getLocale(context));
    context.put(ActionContextConstants.DIALOG_VIEW, collectionView);

    getMvcBinder(context).bind(collectionView.getConnector(),
        componentsModelConnector);

    return super.execute(actionHandler, context);
  }

  /**
   * Sets the cancelAction.
   * 
   * @param cancelAction
   *          the cancelAction to set.
   */
  public void setCancelAction(AbstractChainedAction cancelAction) {
    this.cancelAction = cancelAction;
  }

  /**
   * Sets the okAction.
   * 
   * @param okAction
   *          the okAction to set.
   */
  public void setOkAction(AbstractChainedAction okAction) {
    this.okAction = okAction;
  }
}
