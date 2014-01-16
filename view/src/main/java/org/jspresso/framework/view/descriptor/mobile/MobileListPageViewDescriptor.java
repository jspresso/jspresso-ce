/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view.descriptor.mobile;

import java.util.Collections;
import java.util.List;

import org.jspresso.framework.util.gui.IconProvider;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicCompositeViewDescriptor;

/**
 * List page view descriptors that are able to navigate to another page.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class MobileListPageViewDescriptor extends BasicCompositeViewDescriptor implements IMobilePageViewDescriptor {

  private IconProvider              itemIconImageURLProvider;
  private String                    itemTitleProperty;
  private String                    itemSubtitleProperty;
  private IMobilePageViewDescriptor selectionPage;

  /**
   * Is cascading models.
   *
   * @return always true since a mobile list always cascade models to the next page.
   */
  @Override
  public boolean isCascadingModels() {
    return true;
  }

  /**
   * Sets cascading models. This operation is not supported on mobile lists.
   *
   * @param cascadingModels
   *     the cascading models
   */
  @Override
  public void setCascadingModels(boolean cascadingModels) {
    throw new UnsupportedOperationException("Cannot configure cascading model on Mobile containers");
  }

  @Override
  public List<IViewDescriptor> getChildViewDescriptors() {
    if (selectionPage != null) {
      completeChildDescriptor(selectionPage, this);
    }
    return Collections.singletonList((IViewDescriptor) selectionPage);
  }

  /**
   * Gets item icon image uRL provider.
   *
   * @return the item icon image uRL provider
   */
  public IconProvider getItemIconImageURLProvider() {
    return itemIconImageURLProvider;
  }

  /**
   * Sets item icon image uRL provider.
   *
   * @param itemIconImageURLProvider the item icon image uRL provider
   */
  public void setItemIconImageURLProvider(IconProvider itemIconImageURLProvider) {
    this.itemIconImageURLProvider = itemIconImageURLProvider;
  }

  /**
   * Gets item title property.
   *
   * @return the item title property
   */
  public String getItemTitleProperty() {
    return itemTitleProperty;
  }

  /**
   * Sets item title property.
   *
   * @param itemTitleProperty the item title property
   */
  public void setItemTitleProperty(String itemTitleProperty) {
    this.itemTitleProperty = itemTitleProperty;
  }

  /**
   * Gets item subtitle property.
   *
   * @return the item subtitle property
   */
  public String getItemSubtitleProperty() {
    return itemSubtitleProperty;
  }

  /**
   * Sets item subtitle property.
   *
   * @param itemSubtitleProperty the item subtitle property
   */
  public void setItemSubtitleProperty(String itemSubtitleProperty) {
    this.itemSubtitleProperty = itemSubtitleProperty;
  }

  /**
   * Sets selection page.
   *
   * @param selectionPage
   *     the selection page
   */
  public void setSelectionPage(IMobilePageViewDescriptor selectionPage) {
    this.selectionPage = selectionPage;
  }

  /**
   * Gets selection page.
   *
   * @return the selection page
   */
  public IMobilePageViewDescriptor getSelectionPage() {
    return selectionPage;
  }
}
