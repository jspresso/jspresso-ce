/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.wings.lov;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.wings.std.OkAction;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;


/**
 * Sets the selected component as the value of the source view connector (which
 * will propagate to the backend).
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class OkChooseComponentAction extends OkAction {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    ICollectionConnector resultConnector = ((ICollectionConnectorProvider) getViewConnector(context))
        .getCollectionConnector();
    int[] resultSelectedIndices = resultConnector.getSelectedIndices();
    if (resultSelectedIndices != null && resultSelectedIndices.length > 0) {
      Object selectedComponent = resultConnector.getChildConnector(
          resultSelectedIndices[0]).getConnectorValue();
      context.put(ActionContextConstants.ACTION_PARAM, selectedComponent);
    }
    return super.execute(actionHandler, context);
  }
}
