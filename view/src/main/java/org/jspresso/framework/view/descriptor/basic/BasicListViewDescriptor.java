/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.util.gui.IIconImageURLProvider;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;

/**
 * This type of descriptor is used to implement a list view. A list view is a
 * single column, uneditable collection view used to display a collection of
 * components. Each item is displayed using a string representation that can be
 * customized using the <code>renderedProperty</code> property. List views are
 * rarely used since one might prefer its much more advanced cousin, i.e. the
 * table view.
 * <p>
 * Despite its low usage as an individual UI component, the list view is also
 * used by Jspresso to describe tree parts. A collection of sibling tree nodes
 * can actually be considered as being a list view and can be described as such.
 * In the latter case, the <code>renderedProperty</code> property will be used
 * to label the tree nodes.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicListViewDescriptor extends BasicCollectionViewDescriptor
    implements IListViewDescriptor {

  private IIconImageURLProvider iconImageURLProvider;
  private String                renderedProperty;

  /**
   * Gets the iconImageURLProvider.
   * 
   * @return the iconImageURLProvider.
   */
  public IIconImageURLProvider getIconImageURLProvider() {
    return iconImageURLProvider;
  }

  /**
   * {@inheritDoc}
   */
  public String getRenderedProperty() {
    if (renderedProperty == null) {
      return ((ICollectionDescriptorProvider<?>) getModelDescriptor())
          .getCollectionDescriptor().getElementDescriptor()
          .getToStringProperty();
    }
    return renderedProperty;
  }

  /**
   * The icon image URL provider is the delegate responsible for inferring a
   * tree node icon based on its underlying model. By default (i.e. when
   * <code>iconImageURLProvider</code> is <code>null</code>), Jspresso will use
   * the underlying component descriptor icon, if any. Using a custom icon image
   * URL provider allows to implement finer rules like using different icons
   * based on the underlying object state. There is a single method to implement
   * to achieve this :
   * <p>
   * <code>String getIconImageURLForObject(Object userObject);</code>
   * 
   * @param iconImageURLProvider
   *          the iconImageURLProvider to set.
   */
  public void setIconImageURLProvider(IIconImageURLProvider iconImageURLProvider) {
    this.iconImageURLProvider = iconImageURLProvider;
  }

  /**
   * Configures the model property to be rendered in the list. Whenever this
   * property is left to <code>null</code> (default value), the
   * <code>toStringProperty</code> of the element component descriptor is used.
   * 
   * @param renderedProperty
   *          the renderedProperty to set.
   */
  public void setRenderedProperty(String renderedProperty) {
    this.renderedProperty = renderedProperty;
  }
}
