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
package org.jspresso.framework.model.component;

/**
 * This is an interface used to identify classes responsible for providing
 * component accessors which are not directly related to the core properties.
 * Such classes provide derived (computed) properties of the component.
 *
 * @author Vincent Vandenschrick
 * @param <T>
 *          The class of the extended component.
 */
@SuppressWarnings("EmptyMethod")
public interface IComponentExtension<T extends IComponent> {

  /**
   * Returns the component instance to which this extension is attached.
   *
   * @return The extended entity instance.
   */
  T getComponent();

  /**
   * This method is called when the extension instance is completely created and
   * configured in order to perform extra initialization.
   */
  void postCreate();
}
