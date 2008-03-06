/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor.util;

import java.util.Comparator;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.util.lang.ClassInheritanceComparator;

/**
 * Compares two component descriptors based on their contract inheritance.
 * 
 * @see ClassInheritanceComparator
 *      <p>
 *      Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 *      <p>
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ComponentInheritanceComparator implements
    Comparator<IComponentDescriptor<?>> {

  private ClassInheritanceComparator classInheritanceComparator;

  /**
   * Constructs a new <code>ComponentInheritanceComparator</code> instance.
   * 
   */
  public ComponentInheritanceComparator() {
    classInheritanceComparator = new ClassInheritanceComparator();
  }

  /**
   * {@inheritDoc}
   */
  public int compare(IComponentDescriptor<?> c1, IComponentDescriptor<?> c2) {
    return classInheritanceComparator.compare(c1.getComponentContract(), c2
        .getComponentContract());
  }

}
