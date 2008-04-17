/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.printing.model;

import org.jspresso.framework.util.context.IContextAware;
import org.jspresso.framework.util.descriptor.IDescriptor;

import com.d2s.framework.application.printing.model.descriptor.IReportDescriptor;

/**
 * Defines the contract of a report instance.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IReport extends IContextAware, IDescriptor {

  /**
   * Gets the descriptor of this report.
   * 
   * @return the descriptor of this report.
   */
  IReportDescriptor getReportDescriptor();
}
