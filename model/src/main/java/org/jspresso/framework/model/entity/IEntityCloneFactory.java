/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.entity;

import org.jspresso.framework.model.component.IComponent;

/**
 * A factory to clone entities.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEntityCloneFactory {

  /**
   * Clones a component.
   * 
   * @param <E>
   *            the component contract (class or interface) to clone.
   * @param componentToClone
   *            the component to clone.
   * @param entityFactory
   *            the entity factory to use to create new entities.
   * @return the cloned component.
   */
  <E extends IComponent> E cloneComponent(IComponent componentToClone,
      IEntityFactory entityFactory);

  /**
   * Clones an entity.
   * 
   * @param <E>
   *            the entity contract (class or interface) to clone.
   * @param entityToClone
   *            the entity to clone.
   * @param entityFactory
   *            the entity factory to use to create the new entities.
   * @return the cloned entity.
   */
  <E extends IEntity> E cloneEntity(E entityToClone,
      IEntityFactory entityFactory);
}
