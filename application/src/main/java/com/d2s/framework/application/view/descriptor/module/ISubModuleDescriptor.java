/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.view.descriptor.module;

import com.d2s.framework.application.model.descriptor.module.ModuleDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
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
public interface ISubModuleDescriptor extends ITreeLevelDescriptor {

  /**
   * <code>MODULE_DESCRIPTOR</code> is a unique reference to the model
   * descriptor of modules.
   */
  IComponentDescriptor MODULE_DESCRIPTOR = new ModuleDescriptor();

  /**
   * Gets the projected view descriptor of this module.
   * 
   * @return the projected view descriptor of this module.
   */
  IViewDescriptor getViewDescriptor();
}
