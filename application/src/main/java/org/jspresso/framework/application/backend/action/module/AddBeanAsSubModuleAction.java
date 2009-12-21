/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.util.bean.IPropertyChangeCapable;

/**
 * This action adds the selected objects as child modules.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class AddBeanAsSubModuleAction extends AbstractCollectionAction {

  /**
   * Adds the selected objects as child modules.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    int[] selectedIndices = getSelectedIndices(context);

    if (selectedIndices == null || selectedIndices.length == 0) {
      return false;
    }
    ICollectionConnector collectionConnector = getModelConnector(context);
    Module parentModule = getModule(context);
    List<Module> childModules = parentModule.getSubModules();
    List<Module> newSubModules = new ArrayList<Module>();

    int[] childSelectedIndices = new int[selectedIndices.length];
    for (int i = 0; i < selectedIndices.length; i++) {
      IPropertyChangeCapable nextSelectedModuleObject = (IPropertyChangeCapable) collectionConnector
          .getChildConnector(selectedIndices[i]).getConnectorValue();
      BeanModule nextSubModule = new BeanModule();
      if (parentModule instanceof BeanCollectionModule) {
        nextSubModule
            .setProjectedViewDescriptor(((BeanCollectionModule) parentModule)
                .getElementViewDescriptor());
        nextSubModule
            .setComponentDescriptor(((BeanCollectionModule) parentModule)
                .getElementComponentDescriptor());
      }
      nextSubModule.setModuleObject(nextSelectedModuleObject);
      nextSubModule.setName(String.valueOf(nextSelectedModuleObject));
      int nextSubModuleIndex = -1;
      if (childModules != null) {
        nextSubModuleIndex = childModules.indexOf(nextSubModule);
      }
      if (nextSubModuleIndex < 0) {
        int newSelectedIndex = newSubModules.size();
        if (childModules != null) {
          newSelectedIndex += childModules.size();
        }
        childSelectedIndices[i] = newSelectedIndex;
        newSubModules.add(nextSubModule);
      } else {
        childSelectedIndices[i] = nextSubModuleIndex;
      }
    }
    parentModule.addSubModules(newSubModules);
    setSelectedIndices(childSelectedIndices, context);
    return super.execute(actionHandler, context);
  }
}
