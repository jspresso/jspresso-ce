/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.application.backend.entity;

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
 * This entity invocation handler handles initialization of lazy loaded
 * properties like collections an entity references, delegating the
 * initialization job to the application session.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ApplicationSessionAwareEntityInvocationHandler extends
    BasicEntityInvocationHandler {

  private static final long   serialVersionUID = 3663517052427878204L;

  private IApplicationSession applicationSession;

  /**
   * Constructs a new
   * <code>ApplicationSessionAwareEntityInvocationHandler</code> instance.
   * 
   * @param entityDescriptor
   *          The descriptor of the proxy entity.
   * @param collectionFactory
   *          The factory used to create empty entity collections from
   *          collection getters.
   * @param accessorFactory
   *          The factory used to access proxy properties.
   * @param extensionFactory
   *          The factory used to create entity extensions based on their
   *          classes.
   * @param applicationSession
   *          the current application session.
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
    applicationSession.initializePropertyIfNeeded(
        (IEntity) proxy, propertyDescriptor.getName());
    return super.getReferenceProperty(proxy, propertyDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object getCollectionProperty(Object proxy,
      ICollectionPropertyDescriptor propertyDescriptor) {
    applicationSession.initializePropertyIfNeeded(
        (IEntity) proxy, propertyDescriptor.getName());
    return super.getCollectionProperty(proxy, propertyDescriptor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isInitialized(Object objectOrProxy) {
    return applicationSession.isInitialized(objectOrProxy);
  }
}
