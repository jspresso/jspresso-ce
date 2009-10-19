/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.charting.frontend.action.server;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.charting.frontend.action.AbstractChartAction;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.resources.IResource;
import org.jspresso.framework.util.resources.MemoryResource;
import org.jspresso.framework.util.resources.server.ResourceManager;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;
import org.jspresso.framework.view.action.IDisplayableAction;
import org.springframework.jdbc.core.ConnectionCallback;

/**
 * Displays a fusionchart chart for server-based frontends.
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
public class DisplayChartAction<E, F, G> extends AbstractChartAction<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      final Map<String, Object> context) {
    String chartUrl = ResourceProviderServlet
        .computeLocalResourceDownloadUrl(getChartDescriptor().getUrl());
    String chartData = (String) getJdbcTemplate().execute(
        new ConnectionCallback() {

          public Object doInConnection(Connection con) throws SQLException {
            return getChartDescriptor().getData(getModel(context), con,
                getTranslationProvider(context), getLocale(context));
          }
        });
    IResource resource = new MemoryResource("text/xml", chartData.getBytes());
    String resourceId = ResourceManager.getInstance().register(resource);
    Map<String, String> flashContext = new HashMap<String, String>();
    flashContext.put("dataURL", ResourceProviderServlet
        .computeDownloadUrl(resourceId));
    Dimension d = getChartDescriptor().getDimension();
    flashContext.put("chartWidth", Integer.toString(d.getWidth()));
    flashContext.put("chartHeight", Integer.toString(d.getHeight()));
    List<G> chartActions = new ArrayList<G>();
    for (IDisplayableAction action : getActions()) {
      chartActions.add(getActionFactory(context).createAction(action,
          actionHandler, getSourceComponent(context), null,
          getViewConnector(context), getLocale(context)));
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
}
