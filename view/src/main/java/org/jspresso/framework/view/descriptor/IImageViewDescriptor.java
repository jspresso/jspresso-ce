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
package org.jspresso.framework.view.descriptor;

import org.jspresso.framework.util.image.IScalableImageAware;

/**
 * This public interface is implemented by any image view descriptor.
 *
 * @author Vincent Vandenschrick
 */
public interface IImageViewDescriptor extends IScrollableViewDescriptor, IPropertyViewDescriptor, IScalableImageAware {

  /**
   * Gets whether this image view can be modified by the user drawing in the screen, e.g. a POD signature.
   *
   * @return the boolean
   */
  boolean isDrawable();
}
