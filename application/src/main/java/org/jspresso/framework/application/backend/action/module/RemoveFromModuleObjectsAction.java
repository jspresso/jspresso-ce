/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.application.backend.action.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractCollectionAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.binding.ICollectionConnector;

/**
 * This action, which is to be used on bean collection modules, removes the
 * selected objects from the module's projected collection. If one (or more) of
 * the removed objects are also used in children bean modules, the corresponding
 * children bean modules are also removed accordingly.
 *
 * @author Vincent Vandenschrick
 */
public class RemoveFromModuleObjectsAction extends AbstractCollectionAction {

  /**
   * Constructs a new {@code RemoveFromModuleObjectsAction} instance.
   */
  public RemoveFromModuleObjectsAction() {
    // Disable bad frontend access checks.
    setBadFrontendAccessChecked(false);
  }

  /**
   * Removes the selected objects from the projected collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    int[] selectedIndices = getSelectedIndices(context);
    ICollectionConnector collectionConnector = getModelConnector(context);

    if (selectedIndices == null || selectedIndices.length == 0
        || collectionConnector == null) {
      return false;
    }

    BeanCollectionModule module = (BeanCollectionModule) getModule(context);

    List<Object> projectedCollection;
    if (module.getModuleObjects() == null) {
      projectedCollection = new ArrayList<>();
    } else {
      projectedCollection = new ArrayList<>(module.getModuleObjects());
    }
    for (int selectedIndice : selectedIndices) {
      Object removedObject = collectionConnector.getChildConnector(
          selectedIndice).getConnectorValue();
      projectedCollection.remove(removedObject);
      removeFromSubModules(module, removedObject);
    }
    module.setModuleObjects(projectedCollection);

    getModelConnector(context).setConnectorValue(projectedCollection);
    return super.execute(actionHandler, context);
  }

  private void removeFromSubModules(Module parentModule, Object removedObject) {
    if (parentModule.getSubModules() != null) {
      for (Module module : new ArrayList<>(parentModule.getSubModules())) {
        if (module instanceof BeanModule
            && removedObject.equals(((BeanModule) module).getModuleObject())) {
          parentModule.removeSubModule(module);
        }
      }
    }
  }
}
