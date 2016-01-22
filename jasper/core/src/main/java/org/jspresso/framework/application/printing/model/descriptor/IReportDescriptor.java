/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.application.printing.model.descriptor;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.printing.model.IReport;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;

/**
 * The descriptor of a report. It points to the report design resource as well a
 * the definition of the contextual report parameters.
 *
 * @author Vincent Vandenschrick
 */
public interface IReportDescriptor extends IComponentDescriptor<IReport> {

  /**
   * {@code ENTITY_ID} is "entity_id".
   */
  String ENTITY_ID = "entity_id";

  /**
   * Gets the action, if any, launched before the report gets executed. This
   * action may be used to fill the report context with some arbitrary computed
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
