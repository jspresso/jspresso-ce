/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionDescriptor;
import com.d2s.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import com.d2s.framework.util.IIconImageURLProvider;
import com.d2s.framework.view.descriptor.ITreeLevelDescriptor;
import com.d2s.framework.view.descriptor.ITreeViewDescriptor;

/**
 * Default implementation of a tree view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

  private String                renderedProperty;
  private ITreeLevelDescriptor  childDescriptor;

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
  @SuppressWarnings("unchecked")
  public ITreeLevelDescriptor getRootSubtreeDescriptor() {
    if (rootSubtreeDescriptor == null) {
      BasicCollectionDescriptor<Object> fakeCollDescriptor = new BasicCollectionDescriptor<Object>();
      fakeCollDescriptor
          .setElementDescriptor((IComponentDescriptor<Object>) getModelDescriptor());
      BasicCollectionPropertyDescriptor<Object> fakeCollPropDescriptor = new BasicCollectionPropertyDescriptor<Object>();
      fakeCollPropDescriptor.setReferencedDescriptor(fakeCollDescriptor);
      BasicListViewDescriptor fakeListViewDescriptor = new BasicListViewDescriptor();
      fakeListViewDescriptor.setRenderedProperty(renderedProperty);
      fakeListViewDescriptor.setModelDescriptor(fakeCollPropDescriptor);
      rootSubtreeDescriptor = new BasicSimpleTreeLevelDescriptor();
      ((BasicSimpleTreeLevelDescriptor) rootSubtreeDescriptor)
          .setNodeGroupDescriptor(fakeListViewDescriptor);
      ((BasicSimpleTreeLevelDescriptor) rootSubtreeDescriptor)
          .setChildDescriptor(childDescriptor);
    }
    return rootSubtreeDescriptor;
  }

  /**
   * Sets the iconImageURLProvider.
   * 
   * @param iconImageURLProvider
   *            the iconImageURLProvider to set.
   */
  public void setIconImageURLProvider(IIconImageURLProvider iconImageURLProvider) {
    this.iconImageURLProvider = iconImageURLProvider;
  }

  /**
   * Sets the maxDepth.
   * 
   * @param maxDepth
   *            the maxDepth to set.
   */
  public void setMaxDepth(int maxDepth) {
    this.maxDepth = maxDepth;
  }

  /**
   * Sets the rootSubtreeDescriptor.
   * 
   * @param rootSubtreeDescriptor
   *            the rootSubtreeDescriptor to set.
   */
  public void setRootSubtreeDescriptor(
      ITreeLevelDescriptor rootSubtreeDescriptor) {
    this.rootSubtreeDescriptor = rootSubtreeDescriptor;
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

  /**
   * Sets the renderedProperty.
   * 
   * @param renderedProperty
   *            the renderedProperty to set.
   */
  public void setRenderedProperty(String renderedProperty) {
    this.renderedProperty = renderedProperty;
  }
}
