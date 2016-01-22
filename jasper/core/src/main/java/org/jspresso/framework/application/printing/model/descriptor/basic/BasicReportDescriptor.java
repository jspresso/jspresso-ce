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
package org.jspresso.framework.application.printing.model.descriptor.basic;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.printing.model.IReport;
import org.jspresso.framework.application.printing.model.descriptor.IReportDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicStringPropertyDescriptor;
import org.jspresso.framework.util.descriptor.IDescriptor;

/**
 * Basic implementation of a report descriptor.
 *
 * @author Vincent Vandenschrick
 * @internal
 */
public class BasicReportDescriptor extends BasicComponentDescriptor<IReport>
    implements IReportDescriptor {

  /**
   * The (meta) descriptor of the component descriptor.
   */
  public static final IComponentDescriptor<IReport> INSTANCE = createInstance();
  private IAction                                   beforeAction;

  private String                                    reportDesignUrl;

  /**
   * Constructs a new {@code BasicReportDescriptor} instance.
   *
   * @param name
   *          the name of the report.
   */
  public BasicReportDescriptor(String name) {
    super(name);
  }

  private static IComponentDescriptor<IReport> createInstance() {
    BasicComponentDescriptor<IReport> instance = new BasicComponentDescriptor<>(
        IReport.class.getName());

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<>();

    BasicPropertyDescriptor nameDescriptor = new BasicStringPropertyDescriptor();
    nameDescriptor.setName(IDescriptor.NAME);
    BasicPropertyDescriptor descriptionDescriptor = new BasicStringPropertyDescriptor();
    descriptionDescriptor.setName(IDescriptor.DESCRIPTION);

    propertyDescriptors.add(nameDescriptor);
    propertyDescriptors.add(descriptionDescriptor);

    instance.setPropertyDescriptors(propertyDescriptors);

    return instance;
  }

  /**
   * Gets the beforeAction.
   *
   * @return the beforeAction.
   */
  @Override
  public IAction getBeforeAction() {
    return beforeAction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<? extends IReport> getComponentContract() {
    // return null to have map accessors instead of bean accessors.
    // return IReport.class;
    return null;
  }

  /**
   * Gets the reportDesignUrl.
   *
   * @return the reportDesignUrl.
   */
  @Override
  public String getReportDesignUrl() {
    return reportDesignUrl;
  }

  /**
   * Sets the beforeAction.
   *
   * @param beforeAction
   *          the beforeAction to set.
   */
  public void setBeforeAction(IAction beforeAction) {
    this.beforeAction = beforeAction;
  }

  /**
   * Sets the reportDesignUrl.
   *
   * @param reportDesignUrl
   *          the reportDesignUrl to set.
   */
  public void setReportDesignUrl(String reportDesignUrl) {
    this.reportDesignUrl = reportDesignUrl;
  }

}
