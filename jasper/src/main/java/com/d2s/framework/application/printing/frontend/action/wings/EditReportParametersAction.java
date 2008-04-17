/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.printing.frontend.action.wings;

import java.util.Map;

import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicComponentViewDescriptor;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.application.frontend.action.wings.std.EditComponentAction;
import com.d2s.framework.application.printing.model.IReport;

/**
 * A simple action to edit input parameters in a form view.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
