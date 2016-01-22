/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.util.gui.IconProvider;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;

/**
 * This type of descriptor is used to implement a list view. A list view is a
 * single column, un-editable collection view used to display a collection of
 * components. Each item is displayed using a string representation that can be
 * customized using the {@code renderedProperty} property. List views are
 * rarely used since one might prefer its much more advanced cousin, i.e. the
 * table view.
 * <p>
 * Despite its low usage as an individual UI component, the list view is also
 * used by Jspresso to describe tree parts. A collection of sibling tree nodes
 * can actually be considered as being a list view and can be described as such.
 * In the latter case, the {@code renderedProperty} property will be used
 * to label the tree nodes.
 *
 * @version $LastChangedRevision : 9424 $
 * @author Vincent Vandenschrick
 */
public abstract class AbstractListViewDescriptor extends BasicCollectionViewDescriptor
    implements IListViewDescriptor {

  private IconProvider iconImageURLProvider;
  private String       renderedProperty;
  private boolean      displayIcon;

  /**
   * Instantiates a new Basic list view descriptor.
   */
  public AbstractListViewDescriptor() {
    displayIcon = true;
  }

  /**
   * Gets the iconImageURLProvider.
   *
   * @return the iconImageURLProvider.
   */
  @Override
  public IconProvider getIconImageURLProvider() {
    return iconImageURLProvider;
  }

  /**
   * {@inheritDoc}
   * @return the rendered property
   */
  @Override
  public String getRenderedProperty() {
    if (renderedProperty == null) {
      return ((ICollectionDescriptorProvider<?>) getModelDescriptor()).getCollectionDescriptor().getElementDescriptor()
                                                                      .getToHtmlProperty();
    }
    return renderedProperty;
  }

  /**
   * The icon image URL provider is the delegate responsible for inferring a
   * tree node icon based on its underlying model. By default (i.e. when
   * {@code iconImageURLProvider} is {@code null}), Jspresso will use
   * the underlying component descriptor icon, if any. Using a custom icon image
   * URL provider allows to implement finer rules like using different icons
   * based on the underlying object state. There is a single method to implement
   * to achieve this :
   * <p>
   * {@code String getIconImageURLForObject(Object userObject);}
   *
   * @param iconImageURLProvider           the iconImageURLProvider to set.
   */
  public void setIconImageURLProvider(IconProvider iconImageURLProvider) {
    this.iconImageURLProvider = iconImageURLProvider;
  }

  /**
   * Configures the model property to be rendered in the list. Whenever this
   * property is left to {@code null} (default value), the
   * {@code toStringProperty} of the element component descriptor is used.
   *
   * @param renderedProperty           the renderedProperty to set.
   */
  public void setRenderedProperty(String renderedProperty) {
    this.renderedProperty = renderedProperty;
  }

  /**
   * {@inheritDoc}
   * @return the boolean
   */
  @Override
  public boolean isScrollable() {
    return isVerticallyScrollable() || isHorizontallyScrollable();
  }

  /**
   * Returns {@code true}.
   * <p>
   * {@inheritDoc}
   * @return the boolean
   */
  @Override
  public boolean isVerticallyScrollable() {
    return true;
  }

  /**
   * Returns {@code true}.
   * <p>
   * {@inheritDoc}
   * @return the boolean
   */
  @Override
  public boolean isHorizontallyScrollable() {
    return true;
  }

  /**
   * Will the list view show icons for elements.
   *
   * @return {@code true} whenever the list should show icon.
   */
  @Override
  public boolean isDisplayIcon() {
    return displayIcon;
  }

  /**
   * Configures if the list view should show icon based on the icon image url provider. Defaults to {@code true}.
   *
   * @param displayIcon the show icon
   */
  public void setDisplayIcon(boolean displayIcon) {
    this.displayIcon = displayIcon;
  }
}
