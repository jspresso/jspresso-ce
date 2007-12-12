/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.persistence.hibernate.entity;

import org.hibernate.Hibernate;

import com.d2s.framework.model.entity.SmartEntityCloneFactory;

/**
 * A smart entity clone factory that uses hibernate to determine if an antity or
 * collection is initialized.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class HibernateAwareSmartEntityCloneFactory extends
    SmartEntityCloneFactory {

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isInitialized(Object objectOrProxy) {
    return Hibernate.isInitialized(objectOrProxy);
  }
}
