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

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.util.gui.ERenderingOptions;

/**
 * This public interface is implemented by "Tab" view descriptors. A typical
 * implementation of the described view could be a swing JTabPane.
 *
 * @author Vincent Vandenschrick
 */
public interface ITabViewDescriptor extends ICompositeViewDescriptor {

  /**
   * Gets the renderingOptions.
   *
   * @return the renderingOptions.
   */
  ERenderingOptions getRenderingOptions();

  /**
   * Returns true if this tab view supports lazy tab binding.
   *
   * @return true if this tab view supports lazy tab binding, false otherwise.
   */
  boolean isLazy();

  /**
   * Gets tab selection action.
   *
   * @return the tab selection action
   */
  IAction getTabSelectionAction();
}
