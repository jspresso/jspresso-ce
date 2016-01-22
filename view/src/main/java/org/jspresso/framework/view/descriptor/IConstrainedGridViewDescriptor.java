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

import org.jspresso.framework.util.gui.CellConstraints;

/**
 * A grid view descriptor which organises its components in constrained cells.
 * This kind of described container view might typically be implemented by a
 * swing JPanel with a GridBagLayout.
 *
 * @author Vincent Vandenschrick
 */
public interface IConstrainedGridViewDescriptor extends IGridViewDescriptor {

  /**
   * Gets the constraint applied to a contained view descriptor.
   *
   * @param viewDescriptor
   *          the contained view descriptor.
   * @return the constraints applied to the contained view or null if none.
   */
  CellConstraints getCellConstraints(IViewDescriptor viewDescriptor);
}
