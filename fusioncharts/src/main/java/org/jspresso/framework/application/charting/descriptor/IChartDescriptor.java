/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.charting.descriptor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * Defines a chart.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IChartDescriptor {

  /**
   * Gets the chart url.
   * 
   * @return the chart url.
   */
  String getUrl();

  /**
   * Gets the chart dimensions.
   * 
   * @return the chart dimensions.
   */
  Dimension getDimension();

  /**
   * Gets the chart data.
   * 
   * @param model
   *          the model this chart is ran from.
   * @param jdbcConnection
   *          the JDBC connection to use if this chart has to be run directly
   *          against database.
   * @param translationProvider
   *          the translation provider.
   * @param locale
   *          the locale.
   * @return the chart data.
   * @throws SQLException
   *           whenever an SQL exception occurs while constructing the chart
   *           data.
   */
  String getData(Object model, Connection jdbcConnection,
      ITranslationProvider translationProvider, Locale locale)
      throws SQLException;

  /**
   * Gets the chart title I18N key. This key will be internationalized.
   * 
   * @return the chart title I18N key.
   */
  String getTitle();
}
