/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.printing.model.basic;

import java.util.Map;

import com.d2s.framework.application.printing.model.IReport;
import com.d2s.framework.application.printing.model.descriptor.IReportDescriptor;
import com.d2s.framework.util.descriptor.DefaultDescriptor;

/**
 * A basic report execution instance.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicReport extends DefaultDescriptor implements IReport {

  private IReportDescriptor   reportDescriptor;
  private Map<String, Object> context;

  /**
   * {@inheritDoc}
   */
  public IReportDescriptor getReportDescriptor() {
    return reportDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, Object> getContext() {
    return context;
  }

  /**
   * {@inheritDoc}
   */
  public void setContext(Map<String, Object> context) {
    this.context = context;
  }

  
  /**
   * Sets the reportDescriptor.
   * 
   * @param reportDescriptor the reportDescriptor to set.
   */
  public void setReportDescriptor(IReportDescriptor reportDescriptor) {
    this.reportDescriptor = reportDescriptor;
  }

}
