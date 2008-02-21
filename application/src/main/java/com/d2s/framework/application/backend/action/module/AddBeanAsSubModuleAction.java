/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.backend.action.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.d2s.framework.action.ActionContextConstants;
import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.backend.action.AbstractCollectionAction;
import com.d2s.framework.application.model.BeanModule;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * This action adds the selected objects as child modules.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
  public boolean execute(@SuppressWarnings("unused")
  IActionHandler actionHandler, Map<String, Object> context) {
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
