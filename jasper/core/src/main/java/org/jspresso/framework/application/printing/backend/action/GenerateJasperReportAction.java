/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.printing.backend.action;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.ActionException;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.BackendAction;
import org.jspresso.framework.application.printing.model.IReport;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.util.url.UrlHelper;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Generates a jasper report.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class GenerateJasperReportAction extends BackendAction {

  private JdbcTemplate jdbcTemplate;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    try {
      IReport reportDesign = (IReport) context.get(IReport.REPORT_ACTION_PARAM);
      String urlSpec = reportDesign.getReportDescriptor().getReportDesignUrl();
      final JasperReport jasperReport;
      if (urlSpec.endsWith("xml")) {
        jasperReport = JasperCompileManager.compileReport(UrlHelper.createURL(
            urlSpec).openStream());
      } else {
        jasperReport = (JasperReport) JRLoader.loadObject(UrlHelper.createURL(
            urlSpec).openStream());
      }
      final Map<String, Object> reportContext = new HashMap<String, Object>(
          reportDesign.getContext());
      reportContext.putAll(context);
      UserPrincipal user = getController(context).getApplicationSession()
          .getPrincipal();
      reportContext.putAll(user.getCustomProperties());
      reportContext.put(JRParameter.REPORT_LOCALE, getLocale(context));
      if (reportDesign.getReportDescriptor().getBeforeAction() != null) {
        if (!actionHandler.execute(reportDesign.getReportDescriptor()
            .getBeforeAction(), reportContext)) {
          return false;
        }
      }
      JasperPrint jasperPrint = (JasperPrint) jdbcTemplate
          .execute(new ConnectionCallback() {

            public Object doInConnection(Connection con) {
              try {
                return JasperFillManager.fillReport(jasperReport,
                    reportContext, con);
              } catch (JRException ex) {
                throw new ActionException(ex);
              }
            }
          });
      context.put(ActionContextConstants.ACTION_PARAM, jasperPrint);
    } catch (JRException ex) {
      throw new ActionException(ex);
    } catch (IOException ex) {
      throw new ActionException(ex);
    }
    return super.execute(actionHandler, context);
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
