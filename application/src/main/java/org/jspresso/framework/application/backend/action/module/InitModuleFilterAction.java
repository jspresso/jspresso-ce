/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.module;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractBackendAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.binding.model.IModelValueConnector;


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
    BeanCollectionModule beanCollectionModule = (BeanCollectionModule) getModuleConnector(
        context).getConnectorValue();
    beanCollectionModule.setModuleObjects(null);
    return super.execute(actionHandler, context);
  }

}
