/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.entity;

import java.util.Collection;

import com.d2s.framework.application.backend.session.IApplicationSession;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
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
public class ApplicationSessionAwareEntityInvocationHandler extends
    BasicEntityInvocationHandler {

  private IApplicationSession applicationSession;

  /**
   * Constructs a new <code>ApplicationSessionAwareEntityInvocationHandler</code>
   * instance.
   * 
   * @param entityDescriptor
   * @param collectionFactory
   * @param accessorFactory
   * @param extensionFactory
   */
  protected ApplicationSessionAwareEntityInvocationHandler(
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
   * {@inheritDoc}
   */
  @Override
  protected Object getReferenceProperty(Object proxy,
      IReferencePropertyDescriptor propertyDescriptor) {
    Object lazyProperty = straightGetProperty(propertyDescriptor
        .getName());
    Object initializedProperty = applicationSession
        .initializePropertyIfNeeded((IEntity) proxy, propertyDescriptor
            .getName());
    if (initializedProperty != lazyProperty) {
      storeProperty(propertyDescriptor.getName(), initializedProperty);
    }
    return super.getReferenceProperty(proxy, propertyDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getCollectionProperty(Object proxy,
      ICollectionPropertyDescriptor propertyDescriptor) {
    Object lazyProperty = straightGetProperty(propertyDescriptor
        .getName());
    Object initializedProperty = applicationSession
        .initializePropertyIfNeeded((IEntity) proxy, propertyDescriptor
            .getName());
    if (initializedProperty != lazyProperty) {
      storeProperty(propertyDescriptor.getName(), initializedProperty);
    }
    return super.getCollectionProperty(proxy, propertyDescriptor);
  }

}
