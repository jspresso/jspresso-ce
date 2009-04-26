/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.AbstractCollectionAction;
import org.jspresso.framework.application.model.BeanCollectionModule;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.util.bean.IPropertyChangeCapable;

/**
 * This action adds the selected objects as child modules.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
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
    ICompositeValueConnector moduleConnector = getModuleConnector(context);
    ICollectionConnector collectionConnector = getModelConnector(context);
    Module parentModule = (Module) moduleConnector.getConnectorValue();
    List<Module> children;
    if (parentModule.getSubModules() == null) {
      children = new ArrayList<Module>(selectedIndices.length);
    } else {
      children = new ArrayList<Module>(parentModule.getSubModules());
    }
    int[] childSelectedIndices = new int[selectedIndices.length];
    for (int i = 0; i < selectedIndices.length; i++) {
      IPropertyChangeCapable nextselectedModuleObject = (IPropertyChangeCapable) collectionConnector
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
      nextSubModule.setModuleObject(nextselectedModuleObject);
      nextSubModule.setName(String.valueOf(nextselectedModuleObject));
      int nextSubModuleIndex = children.indexOf(nextSubModule);
      if (nextSubModuleIndex < 0) {
        childSelectedIndices[i] = children.size();
        children.add(nextSubModule);
      } else {
        childSelectedIndices[i] = nextSubModuleIndex;
      }
    }
    parentModule.setSubModules(children);
    context.put(ActionContextConstants.SELECTED_INDICES, childSelectedIndices);
    return super.execute(actionHandler, context);
  }
}
