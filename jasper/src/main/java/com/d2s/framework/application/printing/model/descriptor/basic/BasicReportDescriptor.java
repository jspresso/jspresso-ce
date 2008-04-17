/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.printing.model.descriptor.basic;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicStringPropertyDescriptor;
import org.jspresso.framework.util.descriptor.IDescriptor;

import com.d2s.framework.action.IAction;
import com.d2s.framework.application.printing.model.IReport;
import com.d2s.framework.application.printing.model.descriptor.IReportDescriptor;

/**
 * Basic implementation of a report descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
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
   * Constructs a new <code>BasicReportDescriptor</code> instance.
   * 
   * @param name
   *            the name of the report.
   */
  public BasicReportDescriptor(String name) {
    super(name);
  }

  private static IComponentDescriptor<IReport> createInstance() {
    BasicComponentDescriptor<IReport> instance = new BasicComponentDescriptor<IReport>(
        IReport.class.getName());

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();

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
  public IAction getBeforeAction() {
    return beforeAction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<? extends IReport> getComponentContract() {
    // return null to have map accessors instead of bean accessors.
    //return IReport.class;
    return null;
  }

  /**
   * Gets the reportDesignUrl.
   * 
   * @return the reportDesignUrl.
   */
  public String getReportDesignUrl() {
    return reportDesignUrl;
  }

  /**
   * Sets the beforeAction.
   * 
   * @param beforeAction
   *            the beforeAction to set.
   */
  public void setBeforeAction(IAction beforeAction) {
    this.beforeAction = beforeAction;
  }

  /**
   * Sets the reportDesignUrl.
   * 
   * @param reportDesignUrl
   *            the reportDesignUrl to set.
   */
  public void setReportDesignUrl(String reportDesignUrl) {
    this.reportDesignUrl = reportDesignUrl;
  }

}
