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

import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.application.printing.model.IReport;
import org.jspresso.framework.application.printing.model.descriptor.IReportDescriptor;
import org.jspresso.framework.util.descriptor.DefaultDescriptor;

/**
 * A basic report execution instance.
 *
 * @author Vincent Vandenschrick
 * @internal
 */
public class BasicReport extends DefaultDescriptor implements IReport {

  private final Map<String, Object> context;
  private IReportDescriptor   reportDescriptor;

  /**
   * Constructs a new {@code BasicReport} instance.
   */
  public BasicReport() {
    this.context = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> getContext() {
    return context;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IReportDescriptor getReportDescriptor() {
    return reportDescriptor;
  }

  /**
   * Sets the reportDescriptor.
   *
   * @param reportDescriptor
   *          the reportDescriptor to set.
   */
  public void setReportDescriptor(IReportDescriptor reportDescriptor) {
    this.reportDescriptor = reportDescriptor;
  }

}
