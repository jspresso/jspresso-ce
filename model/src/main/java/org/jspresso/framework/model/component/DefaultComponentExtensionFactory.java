/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
 * This is the default implementation of <code>IComponentExtensionFactory</code>.
 * This implementation relies on Jakarta's <code>ConstructorUtils</code> to
 * build extension instances calling their constructors using reflection.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultComponentExtensionFactory implements
    IComponentExtensionFactory {

  private IComponentFactory componentFactory;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public <E extends IComponent> IComponentExtension<E> createComponentExtension(
      Class<IComponentExtension<E>> extensionClass,
      Class<? extends E> componentContract, E component) {
    try {
      IComponentExtension<E> extension = (IComponentExtension<E>) ConstructorUtils
          .invokeConstructor(extensionClass, new Object[] {component},
              new Class[] {componentContract});
      if (extension instanceof IComponentFactoryAware) {
        ((IComponentFactoryAware) extension)
            .setComponentFactory(componentFactory);
      }
      return extension;
    } catch (NoSuchMethodException ex) {
      throw new ComponentException(ex);
    } catch (IllegalAccessException ex) {
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      throw new ComponentException(ex);
    } catch (InstantiationException ex) {
      throw new ComponentException(ex);
    }
  }

  /**
   * Sets the componentFactory.
   * 
   * @param componentFactory
   *            the componentFactory to set.
   */
  public void setComponentFactory(IComponentFactory componentFactory) {
    this.componentFactory = componentFactory;
  }

}
