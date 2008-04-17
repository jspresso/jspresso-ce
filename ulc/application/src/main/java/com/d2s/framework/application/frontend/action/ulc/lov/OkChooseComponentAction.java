/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.lov;

import java.util.Map;

import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.ulc.std.OkAction;

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
