/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.view.descriptor.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.d2s.framework.application.model.BeanCollectionModule;
import com.d2s.framework.application.model.BeanModule;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.application.model.SubModule;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.basic.AbstractCardViewDescriptor;

/**
 * This is a card view descriptor which stacks the projected view descriptors of
 * the module module structure.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModuleCardViewDescriptor extends AbstractCardViewDescriptor {

  /**
   * Constructs a new <code>ModuleCardViewDescriptor</code> instance.
   * 
   * @param module
   *          the module.
   */
  public ModuleCardViewDescriptor(Module module) {
    Map<String, IViewDescriptor> moduleCards = new HashMap<String, IViewDescriptor>();
    prepareModuleCards(moduleCards, module.getSubModules());
    setCardViewDescriptors(moduleCards);
  }

  /**
   * {@inheritDoc}
   */
  public String getCardNameForModel(Object model) {
    if (model instanceof BeanModule
        && (((BeanModule) model).getParent()) instanceof BeanCollectionModule) {
      return (((BeanModule) model).getParent()).getName() + ".element";
    } else if (model instanceof SubModule) {
      return ((SubModule) model).getName();
    }
    return null;
  }

  private void prepareModuleCards(Map<String, IViewDescriptor> moduleCards,
      List<SubModule> modules) {
    if (modules != null) {
      for (SubModule module : modules) {
        if (module.getViewDescriptor() != null) {
          moduleCards.put(module.getName(), module.getViewDescriptor());
          if (module instanceof BeanCollectionModule
              && ((BeanCollectionModule) module).getElementViewDescriptor() != null) {
            moduleCards.put(module.getName() + ".element",
                ((BeanCollectionModule) module).getElementViewDescriptor());
          }
        }
        prepareModuleCards(moduleCards, module.getSubModules());
      }
    }
  }
}
