/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor.basic;

import java.util.Date;

import org.jspresso.framework.model.descriptor.ITimePropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor;


/**
 * Default implementation of a time descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicTimePropertyDescriptor extends BasicScalarPropertyDescriptor
    implements ITimePropertyDescriptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicTimePropertyDescriptor clone() {
    BasicTimePropertyDescriptor clonedDescriptor = (BasicTimePropertyDescriptor) super
        .clone();

    return clonedDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  public Class<?> getModelType() {
    return Date.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComparableQueryStructureDescriptor createQueryDescriptor() {
    return new ComparableQueryStructureDescriptor(super.createQueryDescriptor());
  }
}
