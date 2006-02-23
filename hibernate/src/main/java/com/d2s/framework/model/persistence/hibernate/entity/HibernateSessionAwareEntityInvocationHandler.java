/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.persistence.hibernate.entity;

import java.util.Collection;

import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.entity.IEntityDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.model.entity.IEntityCollectionFactory;
import com.d2s.framework.model.entity.IEntityExtensionFactory;
import com.d2s.framework.model.entity.basic.BasicEntityInvocationHandler;
import com.d2s.framework.util.bean.IAccessorFactory;

/**
 * TODO Comment needed.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class HibernateSessionAwareEntityInvocationHandler extends
    BasicEntityInvocationHandler {

  private IApplicationSession applicationSession;

  /**
   * Constructs a new <code>HibernateSessionAwareEntityInvocationHandler</code>
   * instance.
   * 
   * @param entityDescriptor
   * @param collectionFactory
   * @param accessorFactory
   * @param extensionFactory
   */
  protected HibernateSessionAwareEntityInvocationHandler(
      IEntityDescriptor entityDescriptor,
      IEntityCollectionFactory collectionFactory,
      IAccessorFactory accessorFactory,
      IEntityExtensionFactory extensionFactory,
      IApplicationSession applicationSession) {
    super(entityDescriptor, collectionFactory, accessorFactory,
        extensionFactory);
    this.applicationSession = applicationSession;
  }

  /**
   * TODO Comment needed.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getCollectionProperty(Object proxy,
      ICollectionPropertyDescriptor propertyDescriptor) {
    Object currentCollection = (Collection<?>) straightGetProperty(propertyDescriptor
        .getName());
    Object initializedCollection = applicationSession
        .initializePropertyIfNeeded((IEntity) proxy, propertyDescriptor
            .getName());
    if (initializedCollection != currentCollection) {
      storeProperty(propertyDescriptor.getName(), initializedCollection);
    }
    return super.getCollectionProperty(proxy, propertyDescriptor);
  }

}
