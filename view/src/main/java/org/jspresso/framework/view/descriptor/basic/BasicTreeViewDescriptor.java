/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.view.descriptor.basic;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicCollectionDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor;
import org.jspresso.framework.util.IIconImageURLProvider;
import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;
import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;


/**
 * Default implementation of a tree view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicTreeViewDescriptor extends BasicViewDescriptor implements
    ITreeViewDescriptor {

  private ITreeLevelDescriptor  childDescriptor;
  private IIconImageURLProvider iconImageURLProvider;
  private int                   maxDepth = 10;

  private String                renderedProperty;
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
      setIconImageURL(iconImageURL);
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
   * Sets the childDescriptor.
   * 
   * @param childDescriptor
   *            the childDescriptor to set.
   */
  public void setChildDescriptor(ITreeLevelDescriptor childDescriptor) {
    this.childDescriptor = childDescriptor;
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
   * Sets the renderedProperty.
   * 
   * @param renderedProperty
   *            the renderedProperty to set.
   */
  public void setRenderedProperty(String renderedProperty) {
    this.renderedProperty = renderedProperty;
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
   * Gets the iconImageURLProvider.
   * 
   * @return the iconImageURLProvider.
   */
  public IIconImageURLProvider getIconImageURLProvider() {
    return iconImageURLProvider;
  }
}
