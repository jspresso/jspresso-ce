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
package org.jspresso.framework.application.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jspresso.framework.view.ICompositeView;
import org.jspresso.framework.view.IMapView;
import org.jspresso.framework.view.IView;

/**
 * Various helper methods for views.
 *
 * @author Vincent Vandenschrick
 */
public final class ViewHelper {

  private ViewHelper() {
    // private constructor for helper class.
  }

  /**
   * Starts from a view and navigates the view hierarchy following an index
   * navigation path.
   *
   * @param <T>
   *     The root class of the view peers.
   * @param fromView
   *     the view to start from.
   * @param viewPath
   *     the view index path to follow.
   *     <ul>
   *     <li>A positive integer n means the nth child.</li>
   *     <li>A negative integer -n means the nth parent.</li>
   *     </ul>
   * @return the view navigated to.
   */
  public static <T> IView<T> navigate(IView<T> fromView, int... viewPath) {
    if (viewPath == null) {
      return null;
    }
    IView<T> target = fromView;
    for (int nextIndex : viewPath) {
      if (target != null) {
        if (nextIndex < 0) {
          for (int j = 0; j > nextIndex; j--) {
            if (target != null) {
              target = target.getParent();
            }
          }
        } else {
          if (target instanceof ICompositeView<?> && ((ICompositeView<?>) target).getChildren() != null
              && nextIndex < ((ICompositeView<?>) target).getChildren().size()) {
            target = ((ICompositeView<T>) target).getChildren().get(nextIndex);
          } else if (target instanceof IMapView<?> && ((IMapView<?>) target).getCurrentView() != null
              && nextIndex == 0) {
            target = ((IMapView<T>) target).getCurrentView();
          } else {
            target = null;
          }
        }
      }
    }
    return target;
  }

  /**
   * Computes a view path from a permId and a starting view.
   *
   * @param permId
   *     the perm id
   * @param fromView
   *     the from view
   * @return the int [ ]
   */
  public static int[] getViewPathFromPermId(String permId, IView<?> fromView) {
    List<Integer> viewPathAsList = new ArrayList<>();
    IView<?> rootView = fromView;
    IView<?> rootViewParent = rootView.getParent();
    while (rootViewParent != null) {
      // Climb up the view hierarchy
      viewPathAsList.add(Integer.valueOf(-1));
      rootView = rootViewParent;
      rootViewParent = rootView.getParent();
    }
    // rootView is now the root of the view hierarchy
    // we must now navigate the children until we find the right permId
    boolean permIdFound = findPermId(permId, rootView, viewPathAsList);
    if (permIdFound) {
      int[] viewPath = new int[viewPathAsList.size()];
      for (int i = 0; i < viewPath.length; i++) {
        viewPath[i] = viewPathAsList.get(i);
      }
      return viewPath;
    }
    return null;
  }

  private static boolean findPermId(String permId, IView<?> fromView, List<Integer> viewPathBuffer) {
    String viewPermId = null;
    if (fromView.getDescriptor() != null) {
      viewPermId = fromView.getDescriptor().getPermId();
    }
    if (permId.equals(viewPermId)) {
      return true;
    }
    List<? extends IView<?>> children = null;
    if (fromView instanceof ICompositeView<?>) {
      children = ((ICompositeView) fromView).getChildren();
    } else if (fromView instanceof IMapView<?>) {
      IView<?> currentView = ((IMapView) fromView).getCurrentView();
      if (currentView != null) {
        children = Arrays.asList(currentView);
      }
    }
    if (children != null) {
      for (int i = 0; i < children.size(); i++) {
        IView<?> child = children.get(i);
        List<Integer> childViewPathBuffer = new ArrayList<>();
        boolean foundInChild = findPermId(permId, child, childViewPathBuffer);
        if (foundInChild) {
          viewPathBuffer.add(Integer.valueOf(i));
          viewPathBuffer.addAll(childViewPathBuffer);
          return true;
        }
      }
    }
    return false;
  }
}
