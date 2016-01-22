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
package org.jspresso.framework.application.printing.model.basic;

import java.util.Locale;

import org.jspresso.framework.application.printing.model.IReport;
import org.jspresso.framework.application.printing.model.IReportFactory;
import org.jspresso.framework.application.printing.model.descriptor.IReportDescriptor;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * Basic implementation of report factory returning instances of
 * {@code BasicReport}.
 *
 * @author Vincent Vandenschrick
 */
public class BasicReportFactory implements IReportFactory {

  /**
   * {@inheritDoc}
   */
  @Override
  public IReport createReportInstance(IReportDescriptor reportDescriptor,
      ITranslationProvider translationProvider, Locale locale) {
    BasicReport report = new BasicReport();
    report.setName(reportDescriptor.getI18nName(translationProvider, locale));
    report.setDescription(reportDescriptor.getI18nDescription(
        translationProvider, locale));
    report.setReportDescriptor(reportDescriptor);
    return report;
  }

}
