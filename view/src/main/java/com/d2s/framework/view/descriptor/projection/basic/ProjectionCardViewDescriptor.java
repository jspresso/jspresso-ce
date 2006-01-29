/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.projection.basic;

import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.view.descriptor.ITreeLevelDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;
import com.d2s.framework.view.descriptor.basic.AbstractCardViewDescriptor;
import com.d2s.framework.view.descriptor.projection.IChildProjectionViewDescriptor;
import com.d2s.framework.view.descriptor.projection.ICompositeChildProjectionViewDescriptor;
import com.d2s.framework.view.descriptor.projection.IProjectionViewDescriptor;
import com.d2s.framework.view.descriptor.projection.ISimpleChildProjectionViewDescriptor;
import com.d2s.framework.view.projection.ChildProjection;

/**
 * This is a card view descriptor which stacks the projected view descriptors of
 * the projection structure.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ProjectionCardViewDescriptor extends AbstractCardViewDescriptor {

  /**
   * Constructs a new <code>ProjectionCardViewDescriptor</code> instance.
   * 
   * @param projectionViewDescriptor
   *          the root projection view descriptor.
   */
  public ProjectionCardViewDescriptor(
      IProjectionViewDescriptor projectionViewDescriptor) {
    IChildProjectionViewDescriptor childProjectionViewDescriptor = (IChildProjectionViewDescriptor) projectionViewDescriptor
        .getRootSubtreeDescriptor();
    Map<String, IViewDescriptor> projectionCards = new HashMap<String, IViewDescriptor>();
    prepareProjectionCards(projectionCards, childProjectionViewDescriptor);
    setViewDescriptors(projectionCards);
  }

  private void prepareProjectionCards(
      Map<String, IViewDescriptor> projectionCards,
      IChildProjectionViewDescriptor projectionViewDescriptor) {
    if (projectionViewDescriptor.getProjectedViewDescriptor() != null) {
      IViewDescriptor projectedObjectViewDescriptor = projectionViewDescriptor
          .getProjectedViewDescriptor();
      projectionCards.put(
          computeKeyForProjectionViewDescriptor(projectionViewDescriptor),
          projectedObjectViewDescriptor);
    }
    if (projectionViewDescriptor instanceof ISimpleChildProjectionViewDescriptor) {
      if (((ISimpleChildProjectionViewDescriptor) projectionViewDescriptor)
          .getChildDescriptor() != null) {
        prepareProjectionCards(
            projectionCards,
            (IChildProjectionViewDescriptor) ((ISimpleChildProjectionViewDescriptor) projectionViewDescriptor)
                .getChildDescriptor());
      }
    } else if (projectionViewDescriptor instanceof ICompositeChildProjectionViewDescriptor) {
      if (((ICompositeChildProjectionViewDescriptor) projectionViewDescriptor)
          .getChildrenDescriptors() != null) {
        for (ITreeLevelDescriptor childProjectionDescriptor : ((ICompositeChildProjectionViewDescriptor) projectionViewDescriptor)
            .getChildrenDescriptors()) {
          prepareProjectionCards(projectionCards,
              (IChildProjectionViewDescriptor) childProjectionDescriptor);
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public String getCardNameForModel(Object model) {
    if (model instanceof ChildProjection) {
      return computeKeyForProjectionViewDescriptor(((ChildProjection) model)
          .getViewDescriptor());
    }
    return null;
  }

  private String computeKeyForProjectionViewDescriptor(
      IChildProjectionViewDescriptor descriptor) {
    if (descriptor.getProjectedViewDescriptor().getName() != null) {
      return descriptor.getProjectedViewDescriptor().getName();
    }
    return descriptor.getProjectedViewDescriptor().toString();
  }
}
