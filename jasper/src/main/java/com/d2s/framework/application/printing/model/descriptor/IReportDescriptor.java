/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.printing.model.descriptor;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;

import com.d2s.framework.action.IAction;
import com.d2s.framework.application.printing.model.IReport;

/**
 * The descriptor of a report. It points to the report design resource as well a
 * the definition of the contextual report parameters.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IReportDescriptor extends IComponentDescriptor<IReport> {

  /**
   * Gets the action, if any, launched before the report gets executed. This
   * action may be used to fill the report context with some bitrary computed
   * data.
   * 
   * @return An action to be triggered before the report gets executed.
   */
  IAction getBeforeAction();

  /**
   * Gets the url to the report design resource.
   * 
   * @return the url to the report design resource.
   */
  String getReportDesignUrl();
}
