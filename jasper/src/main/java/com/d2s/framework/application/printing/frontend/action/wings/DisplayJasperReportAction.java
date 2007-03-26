/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.printing.frontend.action.wings;

import java.util.Map;

import org.wings.SOptionPane;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.wings.AbstractWingsAction;

/**
 * A simple action to display a Jasper report.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DisplayJasperReportAction extends AbstractWingsAction {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    SOptionPane.showMessageDialog(getSourceComponent(context), "N/A", "N/A",
        SOptionPane.INFORMATION_MESSAGE);
    // JasperPrint report = (JasperPrint) context
    // .get(ActionContextConstants.ACTION_PARAM);
    //
    // try {
    // ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // JasperExportManager.exportReportToPdfStream(report, baos);
    //
    // IResource resource = new MemoryResource("application/pdf", baos
    // .toByteArray());
    // String resourceId = ResourceManager.getInstance().register(resource);
    // ResourceManager.getInstance().showDocument(resourceId);
    // } catch (IOException ex) {
    // throw new ActionException(ex);
    // } catch (JRException ex) {
    // throw new ActionException(ex);
    // }
    return true;
  }
}
