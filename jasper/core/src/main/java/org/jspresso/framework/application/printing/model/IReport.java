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

import java.util.Map;

import org.jspresso.framework.application.printing.model.descriptor.IReportDescriptor;
import org.jspresso.framework.util.descriptor.IDescriptor;

/**
 * Defines the contract of a report instance.
 *
 * @author Vincent Vandenschrick
 */
public interface IReport extends IDescriptor {

  /**
   * {@code REPORT_ACTION_PARAM}.
   */
  String REPORT_ACTION_PARAM = "REPORT_ACTION_PARAM";

  /**
   * Gets the report context.
   *
   * @return the context key/value pairs.
   */
  Map<String, Object> getContext();

  /**
   * Gets the descriptor of this report.
   *
   * @return the descriptor of this report.
   */
  IReportDescriptor getReportDescriptor();
}
