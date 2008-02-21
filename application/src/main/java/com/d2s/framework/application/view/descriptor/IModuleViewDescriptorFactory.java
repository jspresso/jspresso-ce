/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.view.descriptor;

import com.d2s.framework.application.model.Module;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * A factory to create view descriptors of modules.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModuleViewDescriptorFactory {

  /**
   * Creates the view descriptor projected by the application module (if any).
   * It gives a chance to the framework to handle custom user-writtent modules
   * (for instance a filterable bean collection module).
   * 
   * @param workspace
   *            the module to create the view descriptor for.
   * @return the created view descriptor.
   */
  IViewDescriptor createProjectedViewDescriptor(Module workspace);
}
