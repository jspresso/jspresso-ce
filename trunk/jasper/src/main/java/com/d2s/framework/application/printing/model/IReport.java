/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.printing.model;

import com.d2s.framework.application.printing.model.descriptor.IReportDescriptor;
import com.d2s.framework.util.context.IContextAware;
import com.d2s.framework.util.descriptor.IDescriptor;

/**
 * Defines the contract of a report instance.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
