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
package org.jspresso.framework.application.printing.model;

import java.util.Locale;

import org.jspresso.framework.application.printing.model.descriptor.IReportDescriptor;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * A factory to create report instances out of report descriptors.
 *
 * @author Vincent Vandenschrick
 */
public interface IReportFactory {

  /**
   * Creates a report instance based on a report descriptor.
   *
   * @param reportDescriptor
   *          the report descriptor to create the report for.
   * @param translationProvider
   *          the translation provider to use.
   * @param locale
   *          the locale to create the report for.
   * @return the created report instance.
   */
  IReport createReportInstance(IReportDescriptor reportDescriptor,
      ITranslationProvider translationProvider, Locale locale);

}
