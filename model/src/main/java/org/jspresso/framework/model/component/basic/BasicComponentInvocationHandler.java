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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import gnu.trove.map.hash.THashMap;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
import org.jspresso.framework.model.component.IComponentExtensionFactory;
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.util.accessor.IAccessorFactory;

/**
 * This is the core implementation of all inline components in the application.
 * Instances of this class serve as handlers for proxies representing the
 * inline components.
 *
 * @author Vincent Vandenschrick
 */
public class BasicComponentInvocationHandler extends
    AbstractComponentInvocationHandler {

  private static final long   serialVersionUID = -3178070064423598514L;

  private final Map<String, Object> properties;

  /**
   * Constructs a new {@code BasicComponentInvocationHandler} instance.
   *
   * @param componentDescriptor
   *          The descriptor of the proxy component.
   * @param inlineComponentFactory
   *          the factory used to create inline components.
   * @param collectionFactory
   *          The factory used to create empty component collections from
   *          collection getters.
   * @param accessorFactory
   *          The factory used to access proxy properties.
   * @param extensionFactory
   *          The factory used to create component extensions based on their
   *          classes.
   */
  public BasicComponentInvocationHandler(
      IComponentDescriptor<IComponent> componentDescriptor,
      IComponentFactory inlineComponentFactory,
      IComponentCollectionFactory collectionFactory,
      IAccessorFactory accessorFactory,
      IComponentExtensionFactory extensionFactory) {
    super(componentDescriptor, inlineComponentFactory, collectionFactory,
        accessorFactory, extensionFactory);
    this.properties = createPropertyMap();
  }

  private Object stackOverFlowEqualsWatchDog;
  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean computeEquals(IComponent proxy, Object another) {
    if (stackOverFlowEqualsWatchDog != null) {
      return another == stackOverFlowEqualsWatchDog;
    }
    if (!(another instanceof IComponent)) {
      return false;
    }
    if (proxy == another) {
      return true;
    }
    stackOverFlowEqualsWatchDog = another;
    try {
      return new EqualsBuilder().append(proxy.getComponentContract(), ((IComponent) another).getComponentContract())
                                .append(proxy.straightGetProperties(), ((IComponent) another).straightGetProperties())
                                .isEquals();
    } finally {
      stackOverFlowEqualsWatchDog = null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected int computeHashCode(IComponent proxy) {
    Map<String, Object> allProperties = proxy.straightGetProperties();
    return new HashCodeBuilder(7, 13).append(proxy.getComponentContract()).append(filterScalarProperties(allProperties))
                                     .toHashCode();
  }

  private Map<String, Object> filterScalarProperties(Map<String, Object> allProperties) {
    for (Iterator<Map.Entry<String, Object>> ite = allProperties.entrySet().iterator(); ite.hasNext(); ) {
      Object val = ite.next().getValue();
      if (val instanceof IComponent || val instanceof Collection) {
        ite.remove();
      }
    }
    return allProperties;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IComponent decorateReferent(IComponent referent,
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
    properties.put(propertyName, refinePropertyToStore(propertyValue));
  }

  private Object stackOverFlowToStringWatchDog;
  /**
   * {@inheritDoc}
   */
  @Override
  protected String toString(Object proxy) {
    try {
      String toString;
      IComponent owningComponent = ((IComponent) proxy).getOwningComponent();
      if (owningComponent != null && stackOverFlowToStringWatchDog == null) {
        stackOverFlowToStringWatchDog = owningComponent;
        toString = owningComponent.toString();
      } else {
        toString = super.toString(proxy);
      }
      return toString;
    } finally {
      stackOverFlowToStringWatchDog = null;
    }
  }


  private Map<String, Object> createPropertyMap() {
    return new THashMap<>(1, 1.0f);
  }
}
