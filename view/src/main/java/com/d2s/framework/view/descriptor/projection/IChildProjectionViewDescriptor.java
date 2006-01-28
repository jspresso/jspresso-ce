/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.projection;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.projection.ProjectionDescriptor;
import com.d2s.framework.view.descriptor.ITreeLevelDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * This interface describes a child projection view.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IChildProjectionViewDescriptor extends ITreeLevelDescriptor {

  /**
   * <code>PROJECTION_DESCRIPTOR</code> is a unique reference to the model
   * descriptor of projections.
   */
  IComponentDescriptor PROJECTION_DESCRIPTOR = new ProjectionDescriptor();

  /**
   * Gets the projected view descriptor of this projection.
   * 
   * @return the projected view descriptor of this projection.
   */
  IViewDescriptor getProjectedViewDescriptor();
}
