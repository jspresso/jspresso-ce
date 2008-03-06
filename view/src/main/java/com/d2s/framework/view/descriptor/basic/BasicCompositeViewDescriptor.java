/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import com.d2s.framework.view.descriptor.ICompositeViewDescriptor;

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
}
