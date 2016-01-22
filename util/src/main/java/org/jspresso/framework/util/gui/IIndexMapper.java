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
package org.jspresso.framework.util.gui;

/**
 * This interface is implemented by classes which provide a mapping between two
 * sets of indices (identified as model and view index sets).
 *
 * @author Vincent Vandenschrick
 */
public interface IIndexMapper {

  /**
   * View to model index translation.
   *
   * @param viewIndex
   *          the view index to translate.
   * @return the resulting model index.
   */
  int modelIndex(int viewIndex);

  /**
   * Model to view index translation.
   *
   * @param modelIndex
   *          the model index to translate.
   * @return the resulting view index.
   */
  int viewIndex(int modelIndex);

}
