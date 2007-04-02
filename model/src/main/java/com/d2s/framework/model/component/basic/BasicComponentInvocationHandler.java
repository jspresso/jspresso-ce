/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.component.basic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.d2s.framework.model.component.ComponentException;
import com.d2s.framework.model.component.IComponent;
import com.d2s.framework.model.component.IComponentCollectionFactory;
import com.d2s.framework.model.component.IComponentExtensionFactory;
import com.d2s.framework.model.component.IComponentFactory;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.entity.basic.BasicEntityInvocationHandler;
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
public class BasicComponentInvocationHandler extends
    AbstractComponentInvocationHandler {

  private static final long serialVersionUID = 4064763209800159366L;

  private IComponentFactory componentFactory;
  private Object            delegate;

  /**
   * Constructs a new <code>BasicComponentInvocationHandler</code> instance.
   *
   * @param delegate
   *          the delegate to which getters and setters are delegated.
   * @param componentFactory
   *          the factory used to decorate referenced components.
   * @param componentDescriptor
   *          The descriptor of the proxy component.
   * @param collectionFactory
   *          The factory used to create empty component collections from
   *          collection getters.
   * @param accessorFactory
   *          The factory used to access proxy properties.
   * @param extensionFactory
   *          The factory used to create component extensions based on their
   *          classes.
   */
  protected BasicComponentInvocationHandler(Object delegate,
      IComponentFactory componentFactory,
      IComponentDescriptor<IComponent> componentDescriptor,
      IComponentCollectionFactory<IComponent> collectionFactory,
      IAccessorFactory accessorFactory,
      IComponentExtensionFactory extensionFactory) {
    super(componentDescriptor, collectionFactory, accessorFactory,
        extensionFactory);
    this.delegate = delegate;
    this.componentFactory = componentFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean computeEquals(IComponent proxy, Object another) {
    if (proxy == another) {
      return true;
    }
    if (another instanceof IComponent) {
      if (Proxy.isProxyClass(another.getClass())
          && Proxy.getInvocationHandler(another) instanceof BasicEntityInvocationHandler) {
        BasicComponentInvocationHandler otherInvocationHandler = (BasicComponentInvocationHandler) Proxy
            .getInvocationHandler(another);
        return delegate.equals(otherInvocationHandler.delegate);
      }
      return delegate.equals(another);
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected int computeHashCode() {
    return delegate.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object retrievePropertyValue(String propertyName) {
    try {
      return getAccessorFactory().createPropertyAccessor(propertyName,
          getComponentContract()).getValue(delegate);
    } catch (IllegalAccessException ex) {
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      throw new ComponentException(ex);
    } catch (NoSuchMethodException ex) {
      throw new ComponentException(ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void storeProperty(String propertyName, Object propertyValue) {
    try {
      getAccessorFactory().createPropertyAccessor(propertyName,
          getComponentContract()).setValue(delegate, propertyValue);
    } catch (IllegalAccessException ex) {
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      throw new ComponentException(ex);
    } catch (NoSuchMethodException ex) {
      throw new ComponentException(ex);
    }
  }

  /**
   * Gives a chance to the delegate to service the method.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object invokeServiceMethod(Object proxy, Method method,
      Object[] args) throws NoSuchMethodException {
    try {
      return super.invokeServiceMethod(proxy, method, args);
    } catch (NoSuchMethodException ex) {
      try {
        return delegate.getClass().getMethod(method.getName(),
            method.getParameterTypes()).invoke(delegate, args);
      } catch (IllegalArgumentException ex1) {
        throw new ComponentException(method.toString()
            + " is not supported on the component " + getComponentContract());
      } catch (IllegalAccessException ex1) {
        throw new ComponentException(method.toString()
            + " is not supported on the component " + getComponentContract());
      } catch (InvocationTargetException ex1) {
        throw new ComponentException(method.toString()
            + " is not supported on the component " + getComponentContract());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IComponent decorateReferent(IComponent referent,
      @SuppressWarnings("unused")
      IComponentDescriptor<IComponent> referentDescriptor) {
    if (Proxy.isProxyClass(referent.getClass())
        && Proxy.getInvocationHandler(referent) instanceof AbstractComponentInvocationHandler) {
      return referent;
    }
    return componentFactory.createComponentInstance(referentDescriptor
        .getComponentContract(), delegate);
  }
}
