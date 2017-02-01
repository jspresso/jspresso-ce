/*
 * Copyright (c) 2005-2017 Vincent Vandenschrick. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IMapView;
import org.jspresso.framework.view.IView;

/**
 * Helper class for property views.
 *
 * @author Vincent Vandenschrick
 */
public final class ViewHelper {

  /**
   * Constructs a new {@code ViewHelper} instance.
   */
  private ViewHelper() {
    // Helper class constructor
  }

  /**
   * Find child views according to view descriptor's perm id.
   *
   * @param permId
   *     The permId.
   * @param root
   *     The root view.
   * @return The List of found view.
   */
  public static List<IView<?>> findChildViews(String permId, IView<?> root) {
    List<IView<?>> found = new ArrayList<>();
    findChildViews(permId, root, found);
    return found;
  }

  private static void findChildViews(String permId, IView<?> parent, List<IView<?>> found) {
    String id = parent.getDescriptor().getPermId();
    if (permId.equals(id)) {
      found.add(parent);
    }
    if (parent instanceof ICompositeView<?>) {
      ICompositeView<?> composite = (ICompositeView<?>) parent;

      for (IView<?> child : composite.getChildren()) {
        findChildViews(permId, child, found);
      }
    } else if (parent instanceof IMapView<?>) {
      IMapView<?> mapView = (IMapView<?>) parent;

      IView<?> currentView = mapView.getCurrentView();
      if (currentView != null) {
        findChildViews(permId, currentView, found);
      }
    }

  }
}
