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
package org.jspresso.framework.application.printing.frontend.action.remote;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.remote.AbstractRemoteAction;
import org.jspresso.framework.util.resources.AbstractActiveResource;
import org.jspresso.framework.util.resources.IActiveResource;
import org.jspresso.framework.util.resources.server.ResourceManager;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;

/**
 * This action will take a {@code JasperPrint} (a processed Jasper report
 * instance), produce a PDF output and open a browser window (tab) to display
 * it.
 *
 * @author Vincent Vandenschrick
 */
public class DisplayJasperReportAction extends AbstractRemoteAction {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    final JasperPrint report = getActionParameter(context);

    IActiveResource pdfProducer = new AbstractActiveResource("application/pdf") {

      @Override
      public String getName() {
        return "Report.pdf";
      }

      @Override
      public long getSize() {
        return -1;
      }

      @Override
      public void writeToContent(OutputStream out) throws IOException {
        try {
          JasperExportManager.exportReportToPdfStream(report, out);
        } catch (JRException ex) {
          IOException ioe = new IOException(ex.getMessage());
          ioe.initCause(ex);
          throw ioe;
        }
      }
    };
    String resourceId = ResourceManager.getInstance().register(pdfProducer);
    getController(context).displayUrl(
        ResourceProviderServlet.computeDownloadUrl(resourceId));
    return super.execute(actionHandler, context);
  }
}
