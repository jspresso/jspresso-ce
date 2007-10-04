/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.test.model;

import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityFactory;
import com.d2s.framework.test.D2STestCase;

/**
 * Test case for entities.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractModelTest extends D2STestCase {

  private IEntityFactory entityFactory;

  /**
   * Creates a new entity instance from its interface contract.
   * 
   * @param <T>
   *            the concrete entity class.
   * @param entityContract
   *            The entity contract.
   * @return The new entity instance.
   */
  protected <T extends IEntity> T createEntityInstance(Class<T> entityContract) {
    return entityFactory.createEntityInstance(entityContract);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "com.d2s.framework.sample.backend.domain";
  }

  /**
   * Gets the entityFactory.
   * 
   * @return the entityFactory.
   */
  protected IEntityFactory getEntityFactory() {
    return entityFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    entityFactory = (IEntityFactory) getApplicationContext().getBean(
        "entityFactory");
  }
}
