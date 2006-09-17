/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.view.descriptor;

import com.d2s.framework.util.descriptor.IIconDescriptor;
import com.d2s.framework.view.descriptor.ISimpleTreeLevelDescriptor;
import com.d2s.framework.view.descriptor.ITreeLevelDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * This interface describes a child module view.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ISubModuleDescriptor extends ISimpleTreeLevelDescriptor,
    ITreeLevelDescriptor, IIconDescriptor {

  /**
   * Gets the projected view descriptor of this module.
   * 
   * @return the projected view descriptor of this module.
   */
  IViewDescriptor getViewDescriptor();
}
