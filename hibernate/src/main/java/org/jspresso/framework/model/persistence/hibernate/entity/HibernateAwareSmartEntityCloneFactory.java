/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.persistence.hibernate.entity;

import org.hibernate.Hibernate;
import org.jspresso.framework.model.entity.SmartEntityCloneFactory;


/**
 * A smart entity clone factory that uses hibernate to determine if an antity or
 * collection is initialized.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
