/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.view.descriptor.module.basic;

import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.application.model.module.SubModule;
import com.d2s.framework.application.view.descriptor.module.ICompositeSubModuleDescriptor;
import com.d2s.framework.application.view.descriptor.module.IModuleDescriptor;
import com.d2s.framework.application.view.descriptor.module.ISimpleSubModuleDescriptor;
import com.d2s.framework.application.view.descriptor.module.ISubModuleDescriptor;
import com.d2s.framework.view.descriptor.ITreeLevelDescriptor;
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
   * @param moduleDescriptor
   *          the module view descriptor.
   */
  public ModuleCardViewDescriptor(IModuleDescriptor moduleDescriptor) {
    ISubModuleDescriptor subModuleDescriptor = (ISubModuleDescriptor) moduleDescriptor
        .getRootSubtreeDescriptor();
    Map<String, IViewDescriptor> moduleCards = new HashMap<String, IViewDescriptor>();
    prepareModuleCards(moduleCards, subModuleDescriptor);
    setCardViewDescriptors(moduleCards);
  }

  private void prepareModuleCards(Map<String, IViewDescriptor> moduleCards,
      ISubModuleDescriptor moduleDescriptor) {
    if (moduleDescriptor.getViewDescriptor() != null) {
      IViewDescriptor projectedObjectViewDescriptor = moduleDescriptor
          .getViewDescriptor();
      moduleCards.put(computeKeyForModuleDescriptor(moduleDescriptor),
          projectedObjectViewDescriptor);
    }
    if (moduleDescriptor instanceof ISimpleSubModuleDescriptor) {
      if (((ISimpleSubModuleDescriptor) moduleDescriptor).getChildDescriptor() != null) {
        prepareModuleCards(
            moduleCards,
            (ISubModuleDescriptor) ((ISimpleSubModuleDescriptor) moduleDescriptor)
                .getChildDescriptor());
      }
    } else if (moduleDescriptor instanceof ICompositeSubModuleDescriptor) {
      if (((ICompositeSubModuleDescriptor) moduleDescriptor)
          .getChildrenDescriptors() != null) {
        for (ITreeLevelDescriptor subModuleDescriptor : ((ICompositeSubModuleDescriptor) moduleDescriptor)
            .getChildrenDescriptors()) {
          prepareModuleCards(moduleCards,
              (ISubModuleDescriptor) subModuleDescriptor);
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public String getCardNameForModel(Object model) {
    if (model instanceof SubModule) {
      return computeKeyForModuleDescriptor(((SubModule) model).getDescriptor());
    }
    return null;
  }

  private String computeKeyForModuleDescriptor(ISubModuleDescriptor descriptor) {
    if (descriptor.getViewDescriptor().getName() != null) {
      return descriptor.getViewDescriptor().getName();
    }
    return descriptor.getViewDescriptor().toString();
  }
}
