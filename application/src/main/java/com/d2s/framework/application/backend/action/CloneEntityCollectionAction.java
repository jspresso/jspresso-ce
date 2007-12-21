/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.application.backend.action;

import java.util.Map;

import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityCloneFactory;

/**
 * An action used duplicate a collection of domain objects. Cloning an entity
 * should result in adding it to the collection the action was triggered on.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CloneEntityCollectionAction extends AbstractCloneCollectionAction {

  private IEntityCloneFactory entityCloneFactory;

  /**
   * Sets the entityCloneFactory.
   * 
   * @param entityCloneFactory
   *            the entityCloneFactory to set.
   */
  public void setEntityCloneFactory(IEntityCloneFactory entityCloneFactory) {
    this.entityCloneFactory = entityCloneFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object cloneElement(Object element, Map<String, Object> context) {
    return entityCloneFactory.cloneEntity((IEntity) element,
        getEntityFactory(context));
  }

}
