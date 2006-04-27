/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.view.descriptor;

import com.d2s.framework.view.action.IAction;
import com.d2s.framework.view.descriptor.ITreeViewDescriptor;

/**
 * This interface describes a module view.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModuleDescriptor extends ITreeViewDescriptor {
  
  /**
   * Gets the action which is executed when the module is launched.
   * 
   * @return the action which is executed when the module is launched.
   */
  IAction getStartupAction();
}
