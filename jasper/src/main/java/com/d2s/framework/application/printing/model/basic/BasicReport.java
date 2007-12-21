/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.printing.model.basic;

import java.util.Map;

import com.d2s.framework.application.printing.model.IReport;
import com.d2s.framework.application.printing.model.descriptor.IReportDescriptor;
import com.d2s.framework.util.descriptor.DefaultDescriptor;

/**
 * A basic report execution instance.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicReport extends DefaultDescriptor implements IReport {

  private Map<String, Object> context;
  private IReportDescriptor   reportDescriptor;

  /**
   * {@inheritDoc}
   */
  public Map<String, Object> getContext() {
    return context;
  }

  /**
   * {@inheritDoc}
   */
  public IReportDescriptor getReportDescriptor() {
    return reportDescriptor;
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
   * @param reportDescriptor
   *            the reportDescriptor to set.
   */
  public void setReportDescriptor(IReportDescriptor reportDescriptor) {
    this.reportDescriptor = reportDescriptor;
  }

}
