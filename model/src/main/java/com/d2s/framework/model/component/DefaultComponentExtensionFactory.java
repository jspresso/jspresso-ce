/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.component;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.ConstructorUtils;

/**
 * This is the default implementation of <code>IComponentExtensionFactory</code>.
 * This implementation relies on Jakarta's <code>ConstructorUtils</code> to
 * build extension instances calling their constructors using reflection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultComponentExtensionFactory implements
    IComponentExtensionFactory {

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public <E extends IComponent> IComponentExtension<E> createComponentExtension(
      Class<IComponentExtension<E>> extensionClass,
      Class<? extends E> componentContract, E component) {
    try {
      return (IComponentExtension<E>) ConstructorUtils.invokeConstructor(
          extensionClass, new Object[] {component},
          new Class[] {componentContract});
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

}
