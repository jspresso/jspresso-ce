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
package org.jspresso.framework.application.printing.frontend.action;

import java.util.Map;

import org.jspresso.framework.application.frontend.action.std.EditComponentAction;
import org.jspresso.framework.application.printing.model.IReport;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicComponentViewDescriptor;

/**
 * This action takes a report ({@code IReport}) from the context (
 * {@code REPORT_ACTION_PARAM} key) and pops-up a form to allow for the
 * report input parameters customization.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class EditReportParametersAction<E, F, G> extends
    EditComponentAction<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getComponentToEdit(Map<String, Object> context) {
    IReport report = (IReport) context.get(IReport.REPORT_ACTION_PARAM);
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
    IReport report = (IReport) context.get(IReport.REPORT_ACTION_PARAM);
    BasicComponentViewDescriptor reportContextViewDescriptor = new BasicComponentViewDescriptor();
    reportContextViewDescriptor
        .setModelDescriptor(report.getReportDescriptor());
    return reportContextViewDescriptor;
  }

}
