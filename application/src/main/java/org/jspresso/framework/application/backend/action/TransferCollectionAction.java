/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.model.datatransfer.ComponentTransferStructure;
import org.jspresso.framework.model.datatransfer.TransferMode;

import com.d2s.framework.action.IActionHandler;

/**
 * An action used register a collection of domain objects into the controller's
 * clipboard.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class TransferCollectionAction extends AbstractCollectionAction {

  private TransferMode transferMode;

  /**
   * Retrieves the managed collection from the model connector then registers
   * the selected elements.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    int[] selectedIndices = getSelectedIndices(context);
    ICollectionConnector collectionConnector = getModelConnector(context);
    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return false;
    }
    List<Object> transferedComponents = new ArrayList<Object>();
    for (int i = 0; i < selectedIndices.length; i++) {
      transferedComponents.add(collectionConnector.getChildConnector(
          selectedIndices[i]).getConnectorValue());
    }
    getController(context).storeComponents(
        new ComponentTransferStructure(getModelDescriptor(context)
            .getCollectionDescriptor().getElementDescriptor(),
            transferedComponents, transferMode));
    return super.execute(actionHandler, context);
  }

  /**
   * Sets the transferMode.
   * 
   * @param transferMode
   *            the transferMode to set.
   */
  public void setTransferMode(TransferMode transferMode) {
    this.transferMode = transferMode;
  }
}
