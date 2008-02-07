/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.backend.action.module;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.action.AbstractBackendAction;
import com.d2s.framework.binding.model.IModelValueConnector;

/**
 * Initialize a module filter with a query entity.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class InitModuleFilterAction extends AbstractBackendAction {

  /**
   * Fills the context with the filter reference descriptor.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    IModelValueConnector filterModelConnector = (IModelValueConnector) getModuleConnector(
        context).getChildConnector("filter");
    context.put(ActionContextConstants.QUERY_MODEL_CONNECTOR,
        filterModelConnector);
    context.put(ActionContextConstants.COMPONENT_REF_DESCRIPTOR,
        filterModelConnector.getModelDescriptor());
    return super.execute(actionHandler, context);
  }

}
