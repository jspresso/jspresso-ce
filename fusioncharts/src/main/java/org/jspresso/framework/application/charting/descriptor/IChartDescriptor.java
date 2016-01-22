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
package org.jspresso.framework.application.charting.descriptor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.i18n.ITranslationProvider;

/**
 * Defines a chart.
 *
 * @author Vincent Vandenschrick
 */
public interface IChartDescriptor {

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
   * Gets the chart dimensions.
   *
   * @return the chart dimensions.
   */
  Dimension getDimension();

  /**
   * Gets the chart title i18n key. This key will be internationalized.
   *
   * @return the chart title i18n key.
   */
  String getTitle();

  /**
   * Gets the chart url.
   *
   * @return the chart url.
   */
  String getUrl();
}
