/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.printing.frontend.action.ulc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.application.frontend.action.ulc.flow.ModalDialogAction;
import com.d2s.framework.application.printing.model.IReport;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.binding.model.IModelConnectorFactory;
import com.d2s.framework.view.IView;
import com.d2s.framework.view.action.IDisplayableAction;
import com.d2s.framework.view.descriptor.basic.BasicComponentViewDescriptor;
import com.ulcjava.base.application.ULCComponent;

/**
 * A simple action to edit input parameters in a form view.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EditReportParametersAction extends ModalDialogAction {

  private IModelConnectorFactory mapConnectorFactory;
  private IDisplayableAction     okAction;
  private IDisplayableAction     cancelAction;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    List<IDisplayableAction> actions = new ArrayList<IDisplayableAction>();

    IReport report = (IReport) context.get(ActionContextConstants.ACTION_PARAM);

    okAction.putInitialContext(ActionContextConstants.SOURCE_VIEW_CONNECTOR,
        getViewConnector(context));
    okAction.putInitialContext(ActionContextConstants.ACTION_PARAM, report);
    actions.add(okAction);
    actions.add(cancelAction);
    context.put(ActionContextConstants.DIALOG_ACTIONS, actions);

    BasicComponentViewDescriptor reportContextViewDescriptor = new BasicComponentViewDescriptor();
    reportContextViewDescriptor
        .setModelDescriptor(report.getReportDescriptor());

    IView<ULCComponent> reportContextView = getViewFactory(context).createView(
        reportContextViewDescriptor, actionHandler, getLocale(context));
    context.put(ActionContextConstants.DIALOG_VIEW, reportContextView);

    IValueConnector reportContextConnector = mapConnectorFactory
        .createModelConnector(report.getReportDescriptor());
    reportContextConnector.setConnectorValue(report.getContext());

    getMvcBinder(context).bind(reportContextView.getConnector(),
        reportContextConnector);

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

  /**
   * Sets the mapConnectorFactory.
   * 
   * @param mapConnectorFactory
   *          the mapConnectorFactory to set.
   */
  public void setMapConnectorFactory(IModelConnectorFactory mapConnectorFactory) {
    this.mapConnectorFactory = mapConnectorFactory;
  }
}
