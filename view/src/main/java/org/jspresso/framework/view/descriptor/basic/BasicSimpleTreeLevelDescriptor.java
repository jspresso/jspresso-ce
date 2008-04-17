/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor.basic;

import org.jspresso.framework.view.descriptor.ISimpleTreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;

/**
 * Basic implementation of a simple subtree view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicSimpleTreeLevelDescriptor extends BasicTreeLevelDescriptor
    implements ISimpleTreeLevelDescriptor {

  private ITreeLevelDescriptor childDescriptor;

  /**
   * {@inheritDoc}
   */
  public ITreeLevelDescriptor getChildDescriptor() {
    return childDescriptor;
  }

  /**
   * Sets the childDescriptor.
   * 
   * @param childDescriptor
   *            the childDescriptor to set.
   */
  public void setChildDescriptor(ITreeLevelDescriptor childDescriptor) {
    this.childDescriptor = childDescriptor;
  }
}
