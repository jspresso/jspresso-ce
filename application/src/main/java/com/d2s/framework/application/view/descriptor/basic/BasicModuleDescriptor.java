/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.view.descriptor.basic;

import com.d2s.framework.view.descriptor.basic.BasicTreeViewDescriptor;

/**
 * This is a default implementation of a simple module view descriptor.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicModuleDescriptor extends BasicTreeViewDescriptor {

  /**
   * Constructs a new <code>BasicModuleDescriptor</code> instance.
   */
  public BasicModuleDescriptor() {
    BasicSubModuleDescriptor subModuleDescriptor = new BasicSubModuleDescriptor();
    subModuleDescriptor.setChildDescriptor(subModuleDescriptor);
    setRootSubtreeDescriptor(subModuleDescriptor);
  }
}
