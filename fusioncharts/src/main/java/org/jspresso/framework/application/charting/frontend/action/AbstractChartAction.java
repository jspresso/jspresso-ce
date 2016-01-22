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
package org.jspresso.framework.application.charting.frontend.action;

import java.util.List;

import org.jspresso.framework.application.charting.descriptor.IChartDescriptor;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * This is the abstract base class for <i>Fusioncharts</i> (flash based charting
 * library) display actions. It holds several common properties that are
 * independent from the actual, concrete, implementations.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class AbstractChartAction<E, F, G> extends FrontendAction<E, F, G> {

  private List<IDisplayableAction> actions;
  private IChartDescriptor         chartDescriptor;
  private JdbcTemplate             jdbcTemplate;

  /**
   * Configures a list of actions to install in the chart modal dialog.
   *
   * @param actions
   *          the actions to set.
   */
  public void setActions(List<IDisplayableAction> actions) {
    this.actions = actions;
  }

  /**
   * This property describes the chart to compute and display. A chart
   * descriptor is a simple data structure that provides :
   * <ul>
   * <li>the URL of the chart SWF archive (can be loaded by the classloader
   * using a classpath pseudo URL)</li>
   * <li>the title of the chart</li>
   * <li>the width/height of the chart area</li>
   * <li>an abstract method to implement in order to compute the chart XML data</li>
   * </ul>
   *
   * @param chartDescriptor
   *          the chartDescriptor to set.
   */
  public void setChartDescriptor(IChartDescriptor chartDescriptor) {
    this.chartDescriptor = chartDescriptor;
  }

  /**
   * Configures the JDBC template to be used by the chart to compute its data.
   *
   * @param jdbcTemplate
   *          the jdbcTemplate to set.
   */
  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Gets the actions.
   *
   * @return the actions.
   */
  protected List<IDisplayableAction> getActions() {
    return actions;
  }

  /**
   * Gets the chartDescriptor.
   *
   * @return the chartDescriptor.
   */
  protected IChartDescriptor getChartDescriptor() {
    return chartDescriptor;
  }

  /**
   * Gets the jdbcTemplate.
   *
   * @return the jdbcTemplate.
   */
  protected JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }
}
