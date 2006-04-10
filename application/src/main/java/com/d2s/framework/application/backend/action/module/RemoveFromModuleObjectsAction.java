/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.action.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.d2s.framework.application.backend.action.AbstractCollectionAction;
import com.d2s.framework.application.module.BeanModule;
import com.d2s.framework.application.module.Module;
import com.d2s.framework.application.module.SubModule;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.util.bean.IPropertyChangeCapable;
import com.d2s.framework.view.action.IActionHandler;

/**
 * This action removes the selected objects from the projected collection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoveFromModuleObjectsAction extends AbstractCollectionAction {

  /**
   * Removes the selected objects from the projected collection.
   * <p>
   * {@inheritDoc}
   */
  public void execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    int[] selectedIndices = getSelectedIndices(context);
    ICollectionConnector collectionConnector = getModelConnector(context);

    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return;
    }

    ICompositeValueConnector moduleConnector = getModuleConnector(context);
    BeanModule module = (BeanModule) moduleConnector.getConnectorValue();

    Collection<IPropertyChangeCapable> projectedCollection;
    if (module.getModuleObjects() == null) {
      projectedCollection = new ArrayList<IPropertyChangeCapable>();
    } else {
      projectedCollection = new ArrayList<IPropertyChangeCapable>(module
          .getModuleObjects());
    }
    for (int i = 0; i < selectedIndices.length; i++) {
      Object removedObject = collectionConnector.getChildConnector(
          selectedIndices[i]).getConnectorValue();
      projectedCollection.remove(removedObject);
      removeFromSubModules(module, removedObject);
    }
    module.setModuleObjects(projectedCollection);

    getModelConnector(context).setConnectorValue(projectedCollection);
  }

  private static void removeFromSubModules(Module parentModule,
      Object removedObject) {
    if (parentModule.getSubModules() != null) {
      for (SubModule subModule : new ArrayList<SubModule>(parentModule
          .getSubModules())) {
        if (subModule instanceof BeanModule
            && removedObject.equals(((BeanModule) subModule).getModuleObject())) {
          parentModule.removeSubModule(subModule);
        }
      }
    }
  }
}
