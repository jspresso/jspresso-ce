/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.printing.model.descriptor.basic;

import java.util.ArrayList;
import java.util.List;

import com.d2s.framework.application.printing.model.IReport;
import com.d2s.framework.application.printing.model.descriptor.IReportDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicComponentDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicPropertyDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicStringPropertyDescriptor;
import com.d2s.framework.util.descriptor.IDescriptor;

/**
 * Basic implementation of a report descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicReportDescriptor extends BasicComponentDescriptor<IReport>
    implements IReportDescriptor {

  private String                                    reportDesignUrl;

  /**
   * The (meta) descriptor of the component descriptor.
   */
  public static final IComponentDescriptor<IReport> INSTANCE = createInstance();

  /**
   * Constructs a new <code>BasicReportDescriptor</code> instance.
   * 
   * @param name
   *          the name of the report.
   */
  public BasicReportDescriptor(String name) {
    super(name);
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
   * {@inheritDoc}
   */
  @Override
  public Class<? extends IReport> getComponentContract() {
    return IReport.class;
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
}
