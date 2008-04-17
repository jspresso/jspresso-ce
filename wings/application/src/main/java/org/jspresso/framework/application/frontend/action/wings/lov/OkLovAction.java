/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.frontend.action.wings.lov;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.session.MergeMode;
import org.jspresso.framework.application.frontend.action.wings.std.OkAction;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.entity.IEntity;


/**
 * Sets the selected entity as the value of the source view connector (which
 * will propagate to the backend).
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class OkLovAction extends OkAction {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    ICollectionConnector resultConnector = (ICollectionConnector) ((ICompositeValueConnector) getViewConnector(context))
        .getChildConnector(IQueryComponent.QUERIED_COMPONENTS);
    int[] resultSelectedIndices = resultConnector.getSelectedIndices();
    if (resultSelectedIndices != null && resultSelectedIndices.length > 0) {
      IEntity selectedEntity = (IEntity) resultConnector.getChildConnector(
          resultSelectedIndices[0]).getConnectorValue();
      if (selectedEntity != null) {
        selectedEntity = getController(context).getApplicationSession().merge(
            selectedEntity, MergeMode.MERGE_KEEP);
      }
      context.put(ActionContextConstants.ACTION_PARAM, selectedEntity);
    }
    return super.execute(actionHandler, context);
  }
}
