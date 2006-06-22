/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.printing.frontend.action;

import java.util.List;

import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.application.printing.model.descriptor.IReportDescriptor;


/**
 * Frontend action to select a report.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class SelectReportAction extends AbstractChainedAction {
  
  private List<IReportDescriptor> reportDescriptors;
  
  /**
   * Sets the reportDescriptors.
   * 
   * @param reportDescriptors the reportDescriptors to set.
   */
  public void setReportDescriptors(List<IReportDescriptor> reportDescriptors) {
    this.reportDescriptors = reportDescriptors;
  }

}
