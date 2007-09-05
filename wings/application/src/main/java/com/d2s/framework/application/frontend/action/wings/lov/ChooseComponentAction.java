/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.wings.lov;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.wings.SComponent;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.wings.std.ModalDialogAction;
import com.d2s.framework.binding.model.IModelValueConnector;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IDisplayableAction;
import com.d2s.framework.view.descriptor.basic.BasicTableViewDescriptor;

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

  private IDisplayableAction cancelAction;
  private IDisplayableAction okAction;

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

    IView<SComponent> collectionView = getViewFactory(context).createView(
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
  public void setCancelAction(IDisplayableAction cancelAction) {
    this.cancelAction = cancelAction;
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
}
