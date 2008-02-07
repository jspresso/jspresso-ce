/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.backend.action.module;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IAction;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.action.AbstractBackendAction;
import com.d2s.framework.application.model.FilterableBeanCollectionModule;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.component.IQueryComponent;

/**
 * Retrieves the filter of a module and queries the persistent store to populate
 * the module objects.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class QueryModuleFilterAction extends AbstractBackendAction {

  private IAction queryAction;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    if (queryAction != null) {
      FilterableBeanCollectionModule module = (FilterableBeanCollectionModule) getModuleConnector(
          context).getConnectorValue();
      IValueConnector filterConnector = getModuleConnector(context)
          .getChildConnector("filter");
      context
          .put(ActionContextConstants.QUERY_MODEL_CONNECTOR, filterConnector);
      IQueryComponent filter = (IQueryComponent) module.getFilter();
      if (actionHandler.execute(queryAction, context)) {
        module.setModuleObjects(filter.getQueriedComponents());
        filter.setQueriedComponents(null);
      }
    }
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the queryAction.
   * 
   * @return the queryAction.
   */
  public IAction getQueryAction() {
    return queryAction;
  }

  /**
   * Sets the queryAction.
   * 
   * @param queryAction
   *            the queryAction to set.
   */
  public void setQueryAction(IAction queryAction) {
    this.queryAction = queryAction;
  }

}
