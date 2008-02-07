/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.frontend.action.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.d2s.framework.action.IActionHandler;
import com.d2s.framework.application.frontend.action.AbstractChainedAction;
import com.d2s.framework.application.model.BeanCollectionModule;
import com.d2s.framework.application.model.BeanModule;
import com.d2s.framework.application.model.SubModule;

/**
 * A simple action which restarts the current module executing the module
 * startup action.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the actual gui component type used.
 * @param <F>
 *            the actual icon type used.
 * @param <G>
 *            the actual action type used.
 */
public class ModuleRestartAction<E, F, G> extends
    AbstractChainedAction<E, F, G> {

  /**
   * Gets the current module and restarts it. {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    SubModule module = (SubModule) getModuleConnector(context)
        .getConnectorValue();
    if (module instanceof BeanCollectionModule
        && module.getSubModules() != null) {
      List<SubModule> beanModulesToRemove = new ArrayList<SubModule>();
      for (SubModule beanModule : module.getSubModules()) {
        if (beanModule instanceof BeanModule) {
          ((BeanModule) beanModule).setModuleObject(null);
          beanModulesToRemove.add(beanModule);
        }
      }
      module.removeSubModules(beanModulesToRemove);
    }
    if (module.getStartupAction() != null) {
      return actionHandler.execute(module.getStartupAction(), context);
    }
    return true;
  }
}
