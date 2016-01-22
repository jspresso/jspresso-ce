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
package org.jspresso.framework.application.charting.frontend.action.server;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.charting.frontend.action.AbstractChartAction;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.lang.StringUtils;
import org.jspresso.framework.util.resources.IResource;
import org.jspresso.framework.util.resources.MemoryResource;
import org.jspresso.framework.util.resources.server.ResourceManager;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.springframework.jdbc.core.ConnectionCallback;

/**
 * This is the concrete implementation of the Fusionchart display action. This
 * action is specialized by UI channel, i.e. server based UI channels (Ajax,
 * Flex, ULC) will use <i>{@code server}</i>
 * {@code .DisplayChartAction} whereas standalone UI channels (Swing) will
 * use <i>{@code standalone}</i>{@code .DisplayChartAction}.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class DisplayChartAction<E, F, G> extends AbstractChartAction<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      final Map<String, Object> context) {
    String chartUrl = ResourceProviderServlet.computeLocalResourceDownloadUrl(
        getChartDescriptor().getUrl(), true);
    String chartData = getJdbcTemplate().execute(
        new ConnectionCallback<String>() {

          @Override
          public String doInConnection(Connection con) throws SQLException {
            return getChartDescriptor().getData(getChartModel(context), con,
                getTranslationProvider(context), getLocale(context));
          }
        });
    IResource resource;
    try {
      resource = new MemoryResource(null, "text/xml", StringUtils
          .prependUtf8Bom(chartData).getBytes(StandardCharsets.UTF_8.name()));
    } catch (UnsupportedEncodingException ex) {
      throw new ActionException(ex);
    }
    String resourceId = ResourceManager.getInstance().register(resource);
    Map<String, String> flashContext = new LinkedHashMap<>();
    Dimension d = getChartDescriptor().getDimension();
    flashContext.put("chartWidth", Integer.toString(d.getWidth() - 20));
    flashContext.put("chartHeight", Integer.toString(d.getHeight() - 100));
    flashContext.put("dataURL",
        ResourceProviderServlet.computeDownloadUrl(resourceId));
    List<G> chartActions = new ArrayList<>();
    for (IDisplayableAction action : getActions()) {
      IView<E> view = getView(context);
      chartActions.add(getActionFactory(context).createAction(action,
          actionHandler, view, getLocale(context)));
    }
    getController(context).displayFlashObject(
        chartUrl,
        flashContext,
        chartActions,
        getTranslationProvider(context).getTranslation(
            getChartDescriptor().getTitle(), getLocale(context)),
        getSourceComponent(context), context, d, false);
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the domain entry point for the chart.
   *
   * @param context
   *          the action context.
   * @return the domain entry point for the chart.
   */
  protected Object getChartModel(Map<String, Object> context) {
    return getSelectedModel(context);
  }
}
