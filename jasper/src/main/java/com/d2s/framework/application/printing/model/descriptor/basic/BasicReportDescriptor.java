/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.printing.model.descriptor.basic;

import com.d2s.framework.application.printing.model.descriptor.IReportDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicComponentDescriptor;

/**
 * Basic implementation of a report descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicReportDescriptor extends BasicComponentDescriptor implements
    IReportDescriptor {

  private String reportDesignUrl;

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
   * Sets the reportDesignUrl.
   * 
   * @param reportDesignUrl
   *          the reportDesignUrl to set.
   */
  public void setReportDesignUrl(String reportDesignUrl) {
    this.reportDesignUrl = reportDesignUrl;
  }

}
