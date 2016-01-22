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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.printing.model.IReport;
import org.jspresso.framework.application.printing.model.IReportFactory;
import org.jspresso.framework.application.printing.model.descriptor.IReportDescriptor;
import org.jspresso.framework.application.printing.model.descriptor.basic.BasicReportDescriptor;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.IModelConnectorFactory;
import org.jspresso.framework.model.descriptor.basic.BasicListDescriptor;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * This action allows the user to choose a report among a list and print it. The
 * list of available reports is statically configured into the action.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class PrintAction<E, F, G> extends FrontendAction<E, F, G> {

  private List<IReportDescriptor> reportDescriptors;
  private IReportFactory          reportFactory;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    BasicListDescriptor<IReport> modelDescriptor = new BasicListDescriptor<>();
    modelDescriptor.setElementDescriptor(BasicReportDescriptor.INSTANCE);
    IValueConnector reportsConnector = getBackendController(context)
        .createModelConnector(ACTION_MODEL_NAME, modelDescriptor);
    reportsConnector.setConnectorValue(createReportInstances(
        getTranslationProvider(context), getLocale(context)));
    setActionParameter(reportsConnector, context);
    return super.execute(actionHandler, context);
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
   * Configures the available report descriptors the user will be allowed to
   * choose.
   *
   * @param reportDescriptors
   *          the reportDescriptors to set.
   */
  public void setReportDescriptors(List<IReportDescriptor> reportDescriptors) {
    this.reportDescriptors = reportDescriptors;
  }

  /**
   * Configures the report factory to use. The report factory is responsible for
   * creating an {@code IReport} (a concrete report instance) from a report
   * descriptor ({@code IReportDescriptor}).
   *
   * @param reportFactory
   *          the reportFactory to set.
   */
  public void setReportFactory(IReportFactory reportFactory) {
    this.reportFactory = reportFactory;
  }

  private List<IReport> createReportInstances(
      ITranslationProvider translationProvider, Locale locale) {
    List<IReport> reports = new ArrayList<>();
    if (reportDescriptors != null) {
      for (IReportDescriptor descriptor : reportDescriptors) {
        reports.add(reportFactory.createReportInstance(descriptor,
            translationProvider, locale));
      }
    }
    return reports;
  }
}
