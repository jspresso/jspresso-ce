/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.ConstructorUtils;

/**
 * This is the default implementation of <code>IEntityExtensionFactory</code>.
 * This implementation reli_es on Jakarta's <code>ConstructorUtils</code> to
 * build extension instances calling their constructors using reflection.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultEntityExtensionFactory implements IEntityExtensionFactory {

  /**
   * {@inheritDoc}
   */
  public IEntityExtension createEntityExtension(Class extensionClass,
      Class entityContract, IEntity entity) {
    try {
      return (IEntityExtension) ConstructorUtils.invokeConstructor(
          extensionClass, new Object[] {entity}, new Class[] {entityContract});
    } catch (NoSuchMethodException ex) {
      throw new EntityException(ex);
    } catch (IllegalAccessException ex) {
      throw new EntityException(ex);
    } catch (InvocationTargetException ex) {
      throw new EntityException(ex);
    } catch (InstantiationException ex) {
      throw new EntityException(ex);
    }
  }

}
