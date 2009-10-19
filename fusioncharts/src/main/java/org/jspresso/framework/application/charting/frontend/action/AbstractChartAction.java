/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.charting.frontend.action;

import java.util.List;

import org.jspresso.framework.application.charting.descriptor.IChartDescriptor;
import org.jspresso.framework.application.frontend.action.WrappingAction;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Abstract base class for chart display action.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class AbstractChartAction<E, F, G> extends WrappingAction<E, F, G> {

  private JdbcTemplate             jdbcTemplate;
  private IChartDescriptor         chartDescriptor;
  private List<IDisplayableAction> actions;

  /**
   * Gets the chartDescriptor.
   * 
   * @return the chartDescriptor.
   */
  protected IChartDescriptor getChartDescriptor() {
    return chartDescriptor;
  }

  /**
   * Sets the chartDescriptor.
   * 
   * @param chartDescriptor
   *          the chartDescriptor to set.
   */
  public void setChartDescriptor(IChartDescriptor chartDescriptor) {
    this.chartDescriptor = chartDescriptor;
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
   * Sets the actions.
   * 
   * @param actions
   *          the actions to set.
   */
  public void setActions(List<IDisplayableAction> actions) {
    this.actions = actions;
  }

  /**
   * Gets the jdbcTemplate.
   * 
   * @return the jdbcTemplate.
   */
  protected JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  /**
   * Sets the jdbcTemplate.
   * 
   * @param jdbcTemplate
   *          the jdbcTemplate to set.
   */
  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
}
