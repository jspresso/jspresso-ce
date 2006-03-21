/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.module;

import com.d2s.framework.view.descriptor.ICompositeTreeLevelDescriptor;

/**
 * This interface is implemented by module views on mutable elements (generally
 * a business object).
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICompositeSubModuleDescriptor extends ISubModuleDescriptor,
    ICompositeTreeLevelDescriptor {
  // No extra operation.
}
