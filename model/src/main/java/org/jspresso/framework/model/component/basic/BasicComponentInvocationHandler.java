/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentCollectionFactory;
import org.jspresso.framework.model.component.IComponentExtensionFactory;
import org.jspresso.framework.model.component.IComponentFactory;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.util.accessor.IAccessorFactory;

/**
 * This is the core implementation of all inlined components in the application.
 * Instances of this class serve as handlers for proxies representing the
 * inlined components.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicComponentInvocationHandler extends
    AbstractComponentInvocationHandler {

  private static final long   serialVersionUID = -3178070064423598514L;

  private final Map<String, Object> properties;

  /**
   * Constructs a new <code>BasiccomponentInvocationHandler</code> instance.
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

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean computeEquals(IComponent proxy, Object another) {
    if (!(another instanceof IComponent)) {
      return false;
    }
    if (proxy == another) {
      return true;
    }
    return new EqualsBuilder()
        .append(proxy.getComponentContract(),
            ((IComponent) another).getComponentContract())
        .append(proxy.straightGetProperties(),
            ((IComponent) another).straightGetProperties()).isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected int computeHashCode(IComponent proxy) {
    return new HashCodeBuilder(7, 13).append(proxy.getComponentContract())
        .append(proxy.straightGetProperties()).toHashCode();
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
    properties.put(propertyName, propertyValue);
  }

  private Map<String, Object> createPropertyMap() {
    return new THashMap<String, Object>(1, 1.0f);
  }
}
