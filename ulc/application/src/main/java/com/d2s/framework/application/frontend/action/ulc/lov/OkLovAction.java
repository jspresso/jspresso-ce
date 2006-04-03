/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.ulc.lov;

import com.d2s.framework.application.IController;
import com.d2s.framework.application.backend.session.MergeMode;
import com.d2s.framework.application.frontend.action.ulc.std.DialogOkAction;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.bean.BeanRefPropertyConnector;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.view.action.ActionContextConstants;
import com.d2s.framework.view.action.IActionHandler;

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
public class OkLovAction extends DialogOkAction {

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(IActionHandler actionHandler) {
    ICollectionConnector resultConnector = ((ICollectionConnectorProvider) ((ICompositeValueConnector) getViewConnector())
        .getChildConnector(BeanRefPropertyConnector.THIS_PROPERTY))
        .getCollectionConnector();
    int[] resultSelectedIndices = resultConnector.getSelectedIndices();
    if (resultSelectedIndices != null && resultSelectedIndices.length > 0) {
      IEntity selectedEntity = (IEntity) resultConnector.getChildConnector(
          resultSelectedIndices[0]).getConnectorValue();
      if (selectedEntity != null && actionHandler instanceof IController) {
        selectedEntity = ((IController) actionHandler).merge(selectedEntity,
            MergeMode.MERGE_KEEP);
      }
      getContext().put(ActionContextConstants.ACTION_RESULT, selectedEntity);
    }
    super.execute(actionHandler);
  }
}
