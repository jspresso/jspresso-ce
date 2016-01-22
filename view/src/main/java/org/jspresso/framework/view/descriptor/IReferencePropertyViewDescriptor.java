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

import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * A property view descriptor used to refine reference property views.
 *
 * @author Vincent Vandenschrick
 */
public interface IReferencePropertyViewDescriptor extends IStringPropertyViewDescriptor {

  /**
   * Returns an optional custom LOV action.
   *
   * @return an optional custom LOV action.
   */
  IDisplayableAction getLovAction();

  /**
   * Is auto complete enabled.
   *
   * @return {@code true} if the field should be considered auto completable.
   *         If {@code false}, then the textfield is made readonly.
   */
  boolean isAutoCompleteEnabled();

}
