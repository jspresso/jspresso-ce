/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.charting.frontend.action.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.CloseDialogAction;
import org.jspresso.framework.application.frontend.action.WrappingAction;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.resources.IResource;
import org.jspresso.framework.util.resources.MemoryResource;
import org.jspresso.framework.util.resources.server.ResourceManager;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;

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
public class DisplayChartAction<E, F, G> extends WrappingAction<E, F, G> {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    String chartUrl = ResourceProviderServlet
        .computeLocalResourceDownloadUrl("classpath:com/fusioncharts/FCF_Column3D.swf");
    String chartData = "<graph caption='Monthly Unit Sales' xAxisName='Month' yAxisName='Units'"
        + " showNames='1' decimalPrecision='0' formatNumberScale='0'>"
        + "<set name='Jan' value='462' color='AFD8F8' />"
        + "<set name='Feb' value='857' color='F6BD0F' />"
        + "<set name='Mar' value='671' color='8BBA00' />"
        + "<set name='Apr' value='494' color='FF8E46' />"
        + "<set name='May' value='761' color='008E8E' />"
        + "<set name='Jun' value='960' color='D64646' />"
        + "<set name='Jul' value='629' color='8E468E' />"
        + "<set name='Aug' value='622' color='588526' />"
        + "<set name='Sep' value='376' color='B3AA00' />"
        + "<set name='Oct' value='494' color='008ED6' />"
        + "<set name='Nov' value='761' color='9D080D' />"
        + "<set name='Dec' value='960' color='A186BE' />" + "</graph>";
    IResource resource = new MemoryResource("text/xml", chartData.getBytes());
    String resourceId = ResourceManager.getInstance().register(resource);
    Map<String, String> flashContext = new HashMap<String, String>();
    flashContext.put("dataURL", ResourceProviderServlet
        .computeDownloadUrl(resourceId));
    Dimension d = new Dimension();
    d.setWidth(600);
    d.setHeight(400);
    flashContext.put("chartWidth", Integer.toString(d.getWidth()));
    flashContext.put("chartHeight", Integer.toString(d.getHeight()));
    List<G> actions = new ArrayList<G>();
    CloseDialogAction<E, F, G> closeAction = new CloseDialogAction<E, F, G>();
    closeAction.setName("close");
    actions.add(getActionFactory(context).createAction(closeAction,
        actionHandler, getSourceComponent(context), null,
        getViewConnector(context), getLocale(context)));

    getController(context).displayFlashObject(chartUrl, flashContext, actions,
        "Test Chart", getSourceComponent(context), context, d, false);
    return super.execute(actionHandler, context);
  }
}
