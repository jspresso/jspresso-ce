/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.printing.frontend.action.swing;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.application.frontend.action.swing.std.EditComponentAction;
import com.d2s.framework.application.printing.model.IReport;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicComponentViewDescriptor;

/**
 * A simple action to edit input parameters in a form view.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class EditReportParametersAction extends EditComponentAction {

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getModel(Map<String, Object> context) {
    IReport report = (IReport) context.get(ActionContextConstants.ACTION_PARAM);
    if (report != null) {
      return report.getContext();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IViewDescriptor getViewDescriptor(Map<String, Object> context) {
    IReport report = (IReport) context.get(ActionContextConstants.ACTION_PARAM);
    BasicComponentViewDescriptor reportContextViewDescriptor = new BasicComponentViewDescriptor();
    reportContextViewDescriptor
        .setModelDescriptor(report.getReportDescriptor());
    return reportContextViewDescriptor;
  }

}
