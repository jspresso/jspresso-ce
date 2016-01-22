/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.model.component.basic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.jspresso.framework.model.component.ComponentException;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
import org.jspresso.framework.model.component.IComponentExtensionFactory;
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.entity.basic.BasicEntityInvocationHandler;
import org.jspresso.framework.util.accessor.IAccessorFactory;

/**
 * This is the core implementation of all entities in the application. Instances
 * of this class serve as handlers for proxies representing the entities.
 *
 * @author Vincent Vandenschrick
 */
public class BasicDelegatingComponentInvocationHandler extends
    AbstractComponentInvocationHandler {

  private static final long serialVersionUID = 4064763209800159366L;

  private final IComponentFactory componentFactory;
  private final Object            delegate;

  /**
   * Constructs a new {@code BasicComponentInvocationHandler} instance.
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
  public BasicDelegatingComponentInvocationHandler(Object delegate,
      IComponentFactory componentFactory,
      IComponentDescriptor<IComponent> componentDescriptor,
      IComponentCollectionFactory collectionFactory,
      IAccessorFactory accessorFactory,
      IComponentExtensionFactory extensionFactory) {
    super(componentDescriptor, componentFactory, collectionFactory,
        accessorFactory, extensionFactory);
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
        BasicDelegatingComponentInvocationHandler otherInvocationHandler = (BasicDelegatingComponentInvocationHandler) Proxy
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
  protected int computeHashCode(IComponent proxy) {
    return delegate.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IComponent decorateReferent(IComponent referent,
      IComponentDescriptor<? extends IComponent> referentDescriptor) {
    if (Proxy.isProxyClass(referent.getClass())
        && Proxy.getInvocationHandler(referent) instanceof AbstractComponentInvocationHandler) {
      return referent;
    }
    Class<? extends IComponent> componentContract = referentDescriptor.getComponentContract();
    return componentFactory.createComponentInstance(
        componentContract, delegate);
  }

  /**
   * Gives a chance to the delegate to service the method.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object invokeServiceMethod(Object proxy, Method method,
      Object... args) throws NoSuchMethodException {
    try {
      return super.invokeServiceMethod(proxy, method, args);
    } catch (NoSuchMethodException ex) {
      try {
        return delegate.getClass()
            .getMethod(method.getName(), method.getParameterTypes())
            .invoke(delegate, args);
      } catch (IllegalArgumentException | IllegalAccessException ex1) {
        throw new ComponentException(method.toString()
            + " is not supported on the component " + getComponentContract());
      } catch (InvocationTargetException ex1) {
        if (ex1.getCause() instanceof RuntimeException) {
          throw (RuntimeException) ex1.getCause();
        }
        throw new ComponentException(ex1.getCause());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object retrievePropertyValue(String propertyName) {
    try {
      return getAccessorFactory().createPropertyAccessor(propertyName,
          getComponentContract()).getValue(delegate);
    } catch (IllegalAccessException | NoSuchMethodException ex) {
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new ComponentException(ex.getCause());
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
    } catch (IllegalAccessException | NoSuchMethodException ex) {
      throw new ComponentException(ex);
    } catch (InvocationTargetException ex) {
      if (ex.getCause() instanceof RuntimeException) {
        throw (RuntimeException) ex.getCause();
      }
      throw new ComponentException(ex.getCause());
    }
  }
}
