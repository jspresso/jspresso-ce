/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.d2s.framework.view.descriptor.ICompositeTreeLevelDescriptor;
import com.d2s.framework.view.descriptor.ITreeLevelDescriptor;

/**
 * Basic implementation of a composite subtree view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicCompositeTreeLevelDescriptor extends BasicTreeLevelDescriptor
    implements ICompositeTreeLevelDescriptor {

  private Map<String, ITreeLevelDescriptor> childrenDescriptors;

  /**
   * {@inheritDoc}
   */
  public ITreeLevelDescriptor getChildDescriptor(String name) {
    if (childrenDescriptors != null) {
      return childrenDescriptors.get(name);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public List<ITreeLevelDescriptor> getChildrenDescriptors() {
    if (childrenDescriptors != null) {
      return new ArrayList<ITreeLevelDescriptor>(childrenDescriptors.values());
    }
    return null;
  }

  /**
   * Sets the childrenDescriptors.
   * 
   * @param childrenDescriptors
   *            the childrenDescriptors to set.
   */
  public void setChildrenDescriptors(
      List<ITreeLevelDescriptor> childrenDescriptors) {
    this.childrenDescriptors = new LinkedHashMap<String, ITreeLevelDescriptor>();
    for (ITreeLevelDescriptor descriptor : childrenDescriptors) {
      // String nodeGroupDescriptorName = descriptor.getNodeGroupDescriptor()
      // .getName();
      // if (nodeGroupDescriptorName == null) {
      String nodeGroupDescriptorName = descriptor.getNodeGroupDescriptor()
          .getModelDescriptor().getName();
      // }
      this.childrenDescriptors.put(nodeGroupDescriptorName, descriptor);
    }
  }
}
