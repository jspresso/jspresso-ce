/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.projection;

import com.d2s.framework.view.descriptor.ICompositeTreeLevelDescriptor;

/**
 * This interface is implemented by projection views on mutable elements
 * (generally a business object).
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ICompositeChildProjectionViewDescriptor extends
    IChildProjectionViewDescriptor, ICompositeTreeLevelDescriptor {
  // No extra operation.
}
