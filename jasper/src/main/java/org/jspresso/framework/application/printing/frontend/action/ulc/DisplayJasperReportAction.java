/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.printing.frontend.action.ulc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.ulc.AbstractUlcAction;
import org.jspresso.framework.util.resources.IResource;
import org.jspresso.framework.util.resources.MemoryResource;
import org.jspresso.framework.util.resources.server.ResourceManager;
import org.jspresso.framework.util.ulc.resource.DocumentHelper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;


/**
 * A simple action to display a Jasper report.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DisplayJasperReportAction extends AbstractUlcAction {

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
      DocumentHelper.showDocument(resourceId);
    } catch (IOException ex) {
      throw new ActionException(ex);
    } catch (JRException ex) {
      throw new ActionException(ex);
    }
    return true;
  }
}
