/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.printing.frontend.action.wings;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.jspresso.framework.util.resources.IResource;
import org.jspresso.framework.util.resources.MemoryResource;
import org.jspresso.framework.util.resources.server.ResourceManager;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;
import org.wings.session.SessionManager;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.ActionException;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.wings.std.DisplayUrlAction;

/**
 * A simple action to display a Jasper report.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DisplayJasperReportAction extends DisplayUrlAction {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    JasperPrint report = (JasperPrint) context
        .get(ActionContextConstants.ACTION_PARAM);

    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JasperExportManager.exportReportToPdfStream(report, baos);

      IResource resource = new MemoryResource("application/pdf", baos
          .toByteArray());
      String resourceId = ResourceManager.getInstance().register(resource);
      context.put(ActionContextConstants.ACTION_PARAM, ResourceProviderServlet
          .computeUrl(SessionManager.getSession().getServletRequest(),
              resourceId));
      return super.execute(actionHandler, context);
    } catch (JRException ex) {
      throw new ActionException(ex);
    }
  }
}
