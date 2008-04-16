/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.List;

import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.view.descriptor.ICompositeViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * Default implementation of a composite view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicCompositeViewDescriptor extends BasicViewDescriptor
    implements ICompositeViewDescriptor {

  private boolean cascadingModels;

  /**
   * {@inheritDoc}
   */
  public boolean isCascadingModels() {
    return cascadingModels;
  }

  /**
   * Sets the cascadingModels.
   * 
   * @param cascadingModels
   *            true if this descriptor is cascading its models based on a
   *            master / detail relationship.
   */
  public void setCascadingModels(boolean cascadingModels) {
    this.cascadingModels = cascadingModels;
  }

  /**
   * If no model is defined on this composite view descriptor, gets the one from
   * its leading child.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IModelDescriptor getModelDescriptor() {
    IModelDescriptor modelDescriptor = super.getModelDescriptor();
    if (isCascadingModels() && modelDescriptor == null) {
      List<IViewDescriptor> childViewDescriptors = getChildViewDescriptors();
      if (childViewDescriptors != null && !childViewDescriptors.isEmpty()) {
        modelDescriptor = childViewDescriptors.get(0).getModelDescriptor();
      }
    }
    return modelDescriptor;
  }
}
