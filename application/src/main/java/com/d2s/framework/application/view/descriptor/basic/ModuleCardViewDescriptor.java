/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.view.descriptor.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.d2s.framework.application.model.BeanCollectionModule;
import com.d2s.framework.application.model.BeanModule;
import com.d2s.framework.application.model.Module;
import com.d2s.framework.application.model.SubModule;
import com.d2s.framework.application.view.descriptor.IModuleViewDescriptorFactory;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.basic.AbstractCardViewDescriptor;

/**
 * This is a card view descriptor which stacks the projected view descriptors of
 * the module module structure.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModuleCardViewDescriptor extends AbstractCardViewDescriptor {

  private static final String ELEMENT_SUFFIX = ".element";

  /**
   * Constructs a new <code>ModuleCardViewDescriptor</code> instance.
   * 
   * @param module
   *            the module.
   * @param moduleDescriptorViewFactory
   *            the view descriptor factory used to create (or decorate) the
   *            sub-modules projected views.
   */
  public ModuleCardViewDescriptor(Module module,
      IModuleViewDescriptorFactory moduleDescriptorViewFactory) {
    Map<String, IViewDescriptor> moduleCards = new HashMap<String, IViewDescriptor>();
    prepareModuleCards(moduleCards, module.getSubModules(),
        moduleDescriptorViewFactory);
    setCardViewDescriptors(moduleCards);
  }

  /**
   * {@inheritDoc}
   */
  public String getCardNameForModel(Object model) {
    if (model instanceof BeanModule
        && (((BeanModule) model).getParent()) instanceof BeanCollectionModule) {
      return (((BeanModule) model).getParent()).getName() + ELEMENT_SUFFIX;
    } else if (model instanceof SubModule) {
      return ((SubModule) model).getName();
    }
    return null;
  }

  private void prepareModuleCards(Map<String, IViewDescriptor> moduleCards,
      List<SubModule> modules,
      IModuleViewDescriptorFactory moduleDescriptorViewFactory) {
    if (modules != null) {
      for (SubModule module : modules) {
        if (module.getProjectedViewDescriptor() != null) {
          moduleCards.put(module.getName(), moduleDescriptorViewFactory
              .createProjectedViewDescriptor(module));
          if (module instanceof BeanCollectionModule
              && ((BeanCollectionModule) module).getElementViewDescriptor() != null) {
            BeanModule fakeBeanModule = new BeanModule();
            fakeBeanModule
                .setProjectedViewDescriptor(((BeanCollectionModule) module)
                    .getElementViewDescriptor());
            fakeBeanModule
                .setComponentDescriptor(((BeanCollectionModule) module)
                    .getElementComponentDescriptor());
            moduleCards.put(module.getName() + ELEMENT_SUFFIX,
                moduleDescriptorViewFactory
                    .createProjectedViewDescriptor(fakeBeanModule));
          }
        }
        prepareModuleCards(moduleCards, module.getSubModules(),
            moduleDescriptorViewFactory);
      }
    }
  }
}
