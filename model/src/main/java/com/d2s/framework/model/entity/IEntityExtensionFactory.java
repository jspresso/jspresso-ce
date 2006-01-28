/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity;

/**
 * This interface establishes the contract implemented by entity extension
 * factories. An entity extension is a delegate instance attached to an entity
 * instance and responsible for providing access on computed properties.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntityExtensionFactory {

  /**
   * Constructs a new extension instance. Entity extension classes must
   * implement constructors in the form of
   * <code>public EntityExtension(EntityContract entity)</code>.
   * 
   * @param extensionClass
   *          The class of the entity extension.
   * @param entityContract
   *          The interface of the entity.
   * @param entity
   *          the entity instance this extension will be attached to.
   * @return The constructed entity extension.
   */
  IEntityExtension createEntityExtension(Class extensionClass,
      Class entityContract, IEntity entity);
}
