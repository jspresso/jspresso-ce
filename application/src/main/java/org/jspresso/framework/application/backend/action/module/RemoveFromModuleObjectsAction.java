/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.application.backend.action.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractCollectionAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;


/**
 * This action removes the selected objects from the projected collection.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoveFromModuleObjectsAction extends AbstractCollectionAction {

  private static void removeFromSubModules(Module parentModule,
      Object removedObject) {
    if (parentModule.getSubModules() != null) {
      for (Module module : new ArrayList<Module>(parentModule
          .getSubModules())) {
        if (module instanceof BeanModule
            && removedObject.equals(((BeanModule) module).getModuleObject())) {
          parentModule.removeSubModule(module);
        }
      }
    }
  }

  /**
   * Removes the selected objects from the projected collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
    int[] selectedIndices = getSelectedIndices(context);
    ICollectionConnector collectionConnector = getModelConnector(context);

    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return false;
    }

    ICompositeValueConnector moduleConnector = getModuleConnector(context);
    BeanCollectionModule module = (BeanCollectionModule) moduleConnector
        .getConnectorValue();

    Collection<Object> projectedCollection;
    if (module.getModuleObjects() == null) {
      projectedCollection = new ArrayList<Object>();
    } else {
      projectedCollection = new ArrayList<Object>(module.getModuleObjects());
    }
    for (int i = 0; i < selectedIndices.length; i++) {
      Object removedObject = collectionConnector.getChildConnector(
          selectedIndices[i]).getConnectorValue();
      projectedCollection.remove(removedObject);
      removeFromSubModules(module, removedObject);
    }
    module.setModuleObjects(projectedCollection);

    getModelConnector(context).setConnectorValue(projectedCollection);
    return super.execute(actionHandler, context);
  }
}
