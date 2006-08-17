/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.lov;

import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.application.frontend.action.ulc.std.OkAction;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.model.ModelRefPropertyConnector;
import com.d2s.framework.model.entity.IEntity;

/**
 * Sets the selected entity as the value of the source view connector (which
 * will propagate to the backend).
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
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
    ICollectionConnector resultConnector = ((ICollectionConnectorProvider) ((ICompositeValueConnector) getViewConnector(context))
        .getChildConnector(ModelRefPropertyConnector.THIS_PROPERTY))
        .getCollectionConnector();
    int[] resultSelectedIndices = resultConnector.getSelectedIndices();
    if (resultSelectedIndices != null && resultSelectedIndices.length > 0) {
      IEntity selectedEntity = (IEntity) resultConnector.getChildConnector(
          resultSelectedIndices[0]).getConnectorValue();
      if (selectedEntity != null) {
        selectedEntity = getController(context).merge(selectedEntity,
            MergeMode.MERGE_KEEP);
      }
      context.put(ActionContextConstants.ACTION_PARAM, selectedEntity);
    }
    return super.execute(actionHandler, context);
  }
}
