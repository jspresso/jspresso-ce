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

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.printing.model.IReport;
import org.jspresso.framework.application.printing.model.descriptor.IReportDescriptor;
import org.jspresso.framework.binding.ICollectionConnector;

/**
 * This action allows the user to select a report to generate on the collection
 * view where it has been installed. The collection backing the view can either
 * be a collection of {@code IReport} or {@code IReportDescriptor}. In
 * the latter situation, the corresponding {@code IReport} instances are
 * created on the fly using the configured report factory.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ReportAction<E, F, G> extends AbstractReportAction<E, F, G> {

  /**
   * Gets the report to execute out of the model connector.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected IReport getReportToExecute(IActionHandler actionHandler,
      Map<String, Object> context) {
    ICollectionConnector viewConnector = (ICollectionConnector) getViewConnector(context);
    int[] selectedIndices = viewConnector.getSelectedIndices();
    ICollectionConnector collectionConnector = (ICollectionConnector) viewConnector
        .getModelConnector();
    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return null;
    }

    Object reportDescriptorOrReport = collectionConnector.getChildConnector(
        selectedIndices[0]).getConnectorValue();
    IReport report = null;
    if (reportDescriptorOrReport instanceof IReport) {
      report = (IReport) reportDescriptorOrReport;
    } else if (reportDescriptorOrReport instanceof IReportDescriptor) {
      report = getReportFactory().createReportInstance(
          (IReportDescriptor) reportDescriptorOrReport,
          getTranslationProvider(context), getLocale(context));
    }
    return report;
  }

}
