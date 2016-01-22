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

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.ConstructorUtils;

/**
 * This is the default implementation of {@code IComponentExtensionFactory}
 * . This implementation relies on Jakarta's {@code ConstructorUtils} to
 * build extension instances calling their constructors using reflection.
 *
 * @author Vincent Vandenschrick
 */
public class DefaultComponentExtensionFactory implements
    IComponentExtensionFactory {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <E extends IComponent> IComponentExtension<E> createComponentExtension(
      Class<IComponentExtension<E>> extensionClass,
      Class<? extends E> componentContract, E component) {
    try {
      IComponentExtension<E> extension = ConstructorUtils
          .invokeConstructor(extensionClass, new Object[] {
            component
          }, new Class<?>[] {
            componentContract
          });
      return extension;
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException ex) {
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new ComponentException(ex.getCause());
    }
  }
}
