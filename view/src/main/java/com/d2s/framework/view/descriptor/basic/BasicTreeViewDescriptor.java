/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import com.d2s.framework.util.IIconImageURLProvider;
import com.d2s.framework.view.descriptor.ITreeLevelDescriptor;
import com.d2s.framework.view.descriptor.ITreeViewDescriptor;

/**
 * Default implementation of a tree view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicTreeViewDescriptor extends BasicViewDescriptor implements
    ITreeViewDescriptor {

  private IIconImageURLProvider iconImageURLProvider;
  private int                   maxDepth = 10;
  private ITreeLevelDescriptor  rootSubtreeDescriptor;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIconImageURL() {
    String iconImageURL = super.getIconImageURL();
    if (iconImageURL == null) {
      iconImageURL = rootSubtreeDescriptor.getNodeGroupDescriptor()
          .getIconImageURL();
    }
    return iconImageURL;
  }

  /**
   * {@inheritDoc}
   */
  public String getIconImageURLForUserObject(Object userObject) {
    if (iconImageURLProvider != null) {
      return iconImageURLProvider.getIconImageURLForObject(userObject);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public int getMaxDepth() {
    return maxDepth;
  }

  /**
   * {@inheritDoc}
   */
  public ITreeLevelDescriptor getRootSubtreeDescriptor() {
    return rootSubtreeDescriptor;
  }

  /**
   * Sets the iconImageURLProvider.
   * 
   * @param iconImageURLProvider
   *          the iconImageURLProvider to set.
   */
  public void setIconImageURLProvider(IIconImageURLProvider iconImageURLProvider) {
    this.iconImageURLProvider = iconImageURLProvider;
  }

  /**
   * Sets the maxDepth.
   * 
   * @param maxDepth
   *          the maxDepth to set.
   */
  public void setMaxDepth(int maxDepth) {
    this.maxDepth = maxDepth;
  }

  /**
   * Sets the rootSubtreeDescriptor.
   * 
   * @param rootSubtreeDescriptor
   *          the rootSubtreeDescriptor to set.
   */
  public void setRootSubtreeDescriptor(
      ITreeLevelDescriptor rootSubtreeDescriptor) {
    this.rootSubtreeDescriptor = rootSubtreeDescriptor;
  }
}
