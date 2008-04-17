/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.module;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractBackendAction;
import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.model.component.IQueryComponent;


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
