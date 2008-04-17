/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.printing.model.basic;

import java.util.Map;

import org.jspresso.framework.application.printing.model.IReport;
import org.jspresso.framework.application.printing.model.descriptor.IReportDescriptor;
import org.jspresso.framework.util.descriptor.DefaultDescriptor;


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
