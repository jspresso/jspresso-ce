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
package org.jspresso.framework.view.descriptor.mobile;

import java.util.Collections;
import java.util.List;

import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * A card view descriptor that aggregates other pages as card.
 *
 * @author Vincent Vandenschrick
 */
public class MobileCardPageViewDescriptor  extends AbstractMobilePageViewDescriptor {

  private ICardViewDescriptor pagesCardViewDescriptor;

  /**
   * Is cascading models.
   *
   * @return always false since a single mobile page does not cascade models.
   */
  @Override
  public boolean isCascadingModels() {
    return true;
  }

  /**
   * Sets cascading models. This operation is not supported on mobile pages.
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
    if (pagesCardViewDescriptor != null) {
      completeChildDescriptor(pagesCardViewDescriptor, null);
    }
    return Collections.singletonList((IViewDescriptor) pagesCardViewDescriptor);
  }

  /**
   * Gets pages.
   *
   * @return the pages
   */
  public ICardViewDescriptor getPagesCardViewDescriptor() {
    return pagesCardViewDescriptor;
  }

  /**
   * Sets pages.
   *
   * @param pagesCardViewDescriptor the pages
   */
  public void setPagesCardViewDescriptor(ICardViewDescriptor pagesCardViewDescriptor) {
    this.pagesCardViewDescriptor = pagesCardViewDescriptor;
  }
}
