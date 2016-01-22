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
 * This interface establishes the contract implemented by component extension
 * factories. A component extension is a delegate instance attached to an
 * component instance and responsible for providing access on computed
 * properties.
 *
 * @author Vincent Vandenschrick
 */
public interface IComponentExtensionFactory {

  /**
   * Constructs a new extension instance. Component extension classes must
   * implement constructors in the form of
   * {@code public ComponentExtension(ComponentContract component)}.
   *
   * @param <E>
   *          the real component type.
   * @param extensionClass
   *          The class of the component extension.
   * @param componentContract
   *          The interface of the component.
   * @param component
   *          the component instance this extension will be attached to.
   * @return The constructed component extension.
   */
  <E extends IComponent> IComponentExtension<E> createComponentExtension(
      Class<IComponentExtension<E>> extensionClass,
      Class<? extends E> componentContract, E component);

}
