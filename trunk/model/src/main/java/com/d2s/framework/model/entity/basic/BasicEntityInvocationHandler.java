/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.entity.basic;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.d2s.framework.model.component.IComponent;
import com.d2s.framework.model.component.IComponentCollectionFactory;
import com.d2s.framework.model.component.IComponentExtensionFactory;
import com.d2s.framework.model.component.basic.AbstractComponentInvocationHandler;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.entity.IEntity;
import com.d2s.framework.util.accessor.IAccessorFactory;

/**
 * This is the core implementation of all entities in the application. Instances
 * of this class serve as handlers for proxies representing the entities.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicEntityInvocationHandler extends
    AbstractComponentInvocationHandler {

  private static final long   serialVersionUID = 6078989823404409653L;

  private Map<String, Object> properties;

  /**
   * Constructs a new <code>BasicEntityInvocationHandler</code> instance.
   * 
   * @param entityDescriptor
   *            The descriptor of the proxy entity.
   * @param collectionFactory
   *            The factory used to create empty entity collections from
   *            collection getters.
   * @param accessorFactory
   *            The factory used to access proxy properties.
   * @param extensionFactory
   *            The factory used to create entity extensions based on their
   *            classes.
   */
  protected BasicEntityInvocationHandler(
      IComponentDescriptor<IComponent> entityDescriptor,
      IComponentCollectionFactory<IComponent> collectionFactory,
      IAccessorFactory accessorFactory,
      IComponentExtensionFactory extensionFactory) {
    super(entityDescriptor, collectionFactory, accessorFactory,
        extensionFactory);
    this.properties = createPropertyMap();
  }

  /**
   * Handles methods invocations on the entity proxy. Either :
   * <li>delegates to one of its extension if the accessed property is
   * registered as being part of an extension
   * <li>handles property access internally
   * <p>
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public synchronized Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {
    String methodName = method.getName();
    if ("isPersistent".equals(methodName)) {
      return new Boolean(((IEntity) proxy).getVersion() != null);
    }
    return super.invoke(proxy, method, args);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean computeEquals(IComponent proxy, Object another) {
    if (proxy == another) {
      return true;
    }
    Object id = straightGetProperty(IEntity.ID);
    if (id == null) {
      return false;
    }
    if (another instanceof IEntity) {
      Object otherId;
      Class<?> otherContract;

      if (Proxy.isProxyClass(another.getClass())
          && Proxy.getInvocationHandler(another) instanceof BasicEntityInvocationHandler) {
        BasicEntityInvocationHandler otherInvocationHandler = (BasicEntityInvocationHandler) Proxy
            .getInvocationHandler(another);
        otherContract = otherInvocationHandler.getComponentContract();
        otherId = otherInvocationHandler.straightGetProperty(IEntity.ID);
      } else {
        otherContract = ((IEntity) another).getContract();
        otherId = ((IEntity) another).getId();
      }
      return new EqualsBuilder().append(getComponentContract(), otherContract)
          .append(id, otherId).isEquals();
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected int computeHashCode() {
    Object id = straightGetProperty(IEntity.ID);
    if (id == null) {
      throw new NullPointerException(
          "Id must be assigned on the entity before its hashcode can be used.");
    }
    return new HashCodeBuilder(3, 17).append(id).toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IComponent decorateReferent(IComponent referent,
      @SuppressWarnings("unused")
      IComponentDescriptor<? extends IComponent> referentDescriptor) {
    return referent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object retrievePropertyValue(String propertyName) {
    return properties.get(propertyName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void storeProperty(String propertyName, Object propertyValue) {
    properties.put(propertyName, propertyValue);
  }

  private Map<String, Object> createPropertyMap() {
    return new HashMap<String, Object>();
  }
}
