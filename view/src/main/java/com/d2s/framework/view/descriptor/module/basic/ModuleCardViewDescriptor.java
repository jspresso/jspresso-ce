/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.module.basic;

import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.view.descriptor.ITreeLevelDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.basic.AbstractCardViewDescriptor;
import com.d2s.framework.view.descriptor.module.ICompositeSubModuleDescriptor;
import com.d2s.framework.view.descriptor.module.IModuleDescriptor;
import com.d2s.framework.view.descriptor.module.ISimpleSubModuleDescriptor;
import com.d2s.framework.view.descriptor.module.ISubModuleDescriptor;
import com.d2s.framework.view.module.SubModule;

/**
 * This is a card view descriptor which stacks the projected view descriptors of
 * the module projection structure.
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
   * @param moduleViewDescriptor
   *          the module view descriptor.
   */
  public ModuleCardViewDescriptor(
      IModuleDescriptor moduleViewDescriptor) {
    ISubModuleDescriptor childProjectionViewDescriptor = (ISubModuleDescriptor) moduleViewDescriptor
        .getRootSubtreeDescriptor();
    Map<String, IViewDescriptor> projectionCards = new HashMap<String, IViewDescriptor>();
    prepareModuleCards(projectionCards, childProjectionViewDescriptor);
    setCardViewDescriptors(projectionCards);
  }

  private void prepareModuleCards(
      Map<String, IViewDescriptor> projectionCards,
      ISubModuleDescriptor projectionViewDescriptor) {
    if (projectionViewDescriptor.getViewDescriptor() != null) {
      IViewDescriptor projectedObjectViewDescriptor = projectionViewDescriptor
          .getViewDescriptor();
      projectionCards.put(
          computeKeyForModuleDescriptor(projectionViewDescriptor),
          projectedObjectViewDescriptor);
    }
    if (projectionViewDescriptor instanceof ISimpleSubModuleDescriptor) {
      if (((ISimpleSubModuleDescriptor) projectionViewDescriptor)
          .getChildDescriptor() != null) {
        prepareModuleCards(
            projectionCards,
            (ISubModuleDescriptor) ((ISimpleSubModuleDescriptor) projectionViewDescriptor)
                .getChildDescriptor());
      }
    } else if (projectionViewDescriptor instanceof ICompositeSubModuleDescriptor) {
      if (((ICompositeSubModuleDescriptor) projectionViewDescriptor)
          .getChildrenDescriptors() != null) {
        for (ITreeLevelDescriptor childProjectionDescriptor : ((ICompositeSubModuleDescriptor) projectionViewDescriptor)
            .getChildrenDescriptors()) {
          prepareModuleCards(projectionCards,
              (ISubModuleDescriptor) childProjectionDescriptor);
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public String getCardNameForModel(Object model) {
    if (model instanceof SubModule) {
      return computeKeyForModuleDescriptor(((SubModule) model)
          .getDescriptor());
    }
    return null;
  }

  private String computeKeyForModuleDescriptor(
      ISubModuleDescriptor descriptor) {
    if (descriptor.getViewDescriptor().getName() != null) {
      return descriptor.getViewDescriptor().getName();
    }
    return descriptor.getViewDescriptor().toString();
  }
}
