/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor;

/**
 * This interface is implemented by descriptors of simpl tree levels. Simple
 * tree levels are tree levels where children are organised into a single group.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ISimpleTreeLevelDescriptor extends ITreeLevelDescriptor {

  /**
   * Gets the subtree descriptor.
   * 
   * @return the subtree descriptor.
   */
  ITreeLevelDescriptor getChildDescriptor();
}
