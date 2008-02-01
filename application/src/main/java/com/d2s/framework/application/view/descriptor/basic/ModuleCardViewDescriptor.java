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
import com.d2s.framework.application.model.descriptor.BeanCollectionModuleDescriptor;
import com.d2s.framework.application.model.descriptor.BeanModuleDescriptor;
import com.d2s.framework.model.descriptor.ICollectionDescriptorProvider;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.view.descriptor.ICollectionViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.basic.AbstractCardViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicNestingViewDescriptor;
import com.d2s.framework.view.descriptor.basic.BasicViewDescriptor;

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
      return (((BeanModule) model).getParent()).getName() + ELEMENT_SUFFIX;
    } else if (model instanceof SubModule) {
      return ((SubModule) model).getName();
    }
    return null;
  }

  private void prepareModuleCards(Map<String, IViewDescriptor> moduleCards,
      List<SubModule> modules) {
    if (modules != null) {
      for (SubModule module : modules) {
        if (module.getProjectedViewDescriptor() != null) {
          moduleCards.put(module.getName(), decorateModuleCard(module));
          if (module instanceof BeanCollectionModule
              && ((BeanCollectionModule) module).getElementViewDescriptor() != null) {
            moduleCards.put(module.getName() + ELEMENT_SUFFIX,
                decorateElementModuleCard((BeanCollectionModule) module));
          }
        }
        prepareModuleCards(moduleCards, module.getSubModules());
      }
    }
  }

  @SuppressWarnings("unchecked")
  private IViewDescriptor decorateModuleCard(SubModule module) {
    IViewDescriptor projectedViewDescriptor = module
        .getProjectedViewDescriptor();
    if (module instanceof BeanCollectionModule) {
      IComponentDescriptor<Object> componentDescriptor = ((BeanCollectionModule) module)
          .getElementComponentDescriptor();
      if (componentDescriptor == null
          && projectedViewDescriptor instanceof ICollectionViewDescriptor
          && projectedViewDescriptor.getModelDescriptor() != null) {
        componentDescriptor = ((ICollectionDescriptorProvider) projectedViewDescriptor
            .getModelDescriptor()).getCollectionDescriptor()
            .getElementDescriptor();
      }
      BeanCollectionModuleDescriptor moduleDescriptor = new BeanCollectionModuleDescriptor(
          componentDescriptor);
      ((BasicViewDescriptor) projectedViewDescriptor)
          .setModelDescriptor(moduleDescriptor
              .getPropertyDescriptor("moduleObjects"));
      BasicNestingViewDescriptor moduleViewDescriptor = new BasicNestingViewDescriptor();
      moduleViewDescriptor.setNestedViewDescriptor(projectedViewDescriptor);
      moduleViewDescriptor.setModelDescriptor(moduleDescriptor);
      return moduleViewDescriptor;
    } else if (module instanceof BeanModule) {
      IComponentDescriptor<Object> componentDescriptor = ((BeanModule) module)
          .getComponentDescriptor();
      if (componentDescriptor == null
          && projectedViewDescriptor.getModelDescriptor() instanceof IComponentDescriptor) {
        componentDescriptor = (IComponentDescriptor<Object>) projectedViewDescriptor
            .getModelDescriptor();
      }
      BeanModuleDescriptor moduleDescriptor = new BeanModuleDescriptor(
          componentDescriptor);
      ((BasicViewDescriptor) projectedViewDescriptor)
          .setModelDescriptor(moduleDescriptor
              .getPropertyDescriptor("moduleObject"));
      BasicNestingViewDescriptor moduleElementViewDescriptor = new BasicNestingViewDescriptor();
      moduleElementViewDescriptor
          .setNestedViewDescriptor(projectedViewDescriptor);
      moduleElementViewDescriptor.setModelDescriptor(moduleDescriptor);
      return moduleElementViewDescriptor;
    }
    return projectedViewDescriptor;
  }

  @SuppressWarnings("unchecked")
  private IViewDescriptor decorateElementModuleCard(BeanCollectionModule module) {
    BasicViewDescriptor projectedElementViewDescriptor = (BasicViewDescriptor) module
        .getElementViewDescriptor();
    IComponentDescriptor<Object> componentDescriptor = module
        .getElementComponentDescriptor();
    if (componentDescriptor == null
        && projectedElementViewDescriptor.getModelDescriptor() instanceof IComponentDescriptor) {
      componentDescriptor = (IComponentDescriptor<Object>) projectedElementViewDescriptor
          .getModelDescriptor();
    }
    BeanModuleDescriptor moduleDescriptor = new BeanModuleDescriptor(
        componentDescriptor);
    projectedElementViewDescriptor.setModelDescriptor(moduleDescriptor
        .getPropertyDescriptor("moduleObject"));
    BasicNestingViewDescriptor moduleElementViewDescriptor = new BasicNestingViewDescriptor();
    moduleElementViewDescriptor
        .setNestedViewDescriptor(projectedElementViewDescriptor);
    moduleElementViewDescriptor.setModelDescriptor(moduleDescriptor);
    return moduleElementViewDescriptor;
  }
}
