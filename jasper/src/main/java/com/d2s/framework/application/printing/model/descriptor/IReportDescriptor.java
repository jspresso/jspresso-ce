/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.printing.model.descriptor;

import com.d2s.framework.application.printing.model.IReport;
import com.d2s.framework.model.descriptor.IComponentDescriptor;

/**
 * The descriptor of a report. It points to the report design resource as well a
 * the definition of the contextual report parameters.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IReportDescriptor extends IComponentDescriptor<IReport> {

  /**
   * Gets the url to the report design resource.
   * 
   * @return the url to the report design resource.
   */
  String getReportDesignUrl();
}
