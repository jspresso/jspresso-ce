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
package org.jspresso.framework.model.persistence.hibernate.entity.tuplizer;

import java.io.Serializable;

import org.hibernate.AssertionFailure;
import org.hibernate.mapping.Component;
import org.hibernate.tuple.Instantiator;
import org.hibernate.tuple.component.PojoComponentTuplizer;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IComponentFactory;

/**
 * A specialized hibernate tuplizer to handle proxy components.
 *
 * @author Vincent Vandenschrick
 */
public class ProxyPojoComponentTuplizer extends PojoComponentTuplizer {

  private static IComponentFactory inlineComponentFactory;
  private static final long        serialVersionUID = 519439258744322320L;

  /**
   * Constructs a new {@code ProxyPojoComponentTuplizer} instance.
   *
   * @param component
   *          the component to build the proxy for.
   */
  public ProxyPojoComponentTuplizer(Component component) {
    super(component);
  }

  /**
   * Sets the inlineComponentFactory.
   *
   * @param inlineComponentFactory
   *          the inlineComponentFactory to set.
   */
  public static void setInlineComponentFactory(
      IComponentFactory inlineComponentFactory) {
    ProxyPojoComponentTuplizer.inlineComponentFactory = inlineComponentFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Instantiator buildInstantiator(Component component) {
    return new ProxyInstantiator(component);
  }

  private static class ProxyInstantiator implements Instantiator {

    private static final long                 serialVersionUID = 8746568183869210457L;
    private final Class<? extends IComponent> componentContract;

    /**
     * Constructs a new {@code ProxyInstantiator} instance.
     *
     * @param component
     *          the hibernate component to build this instantiator for.
     */
    @SuppressWarnings("unchecked")
    public ProxyInstantiator(Component component) {
      componentContract = component.getComponentClass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object instantiate() {
      return inlineComponentFactory.createComponentInstance(componentContract);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object instantiate(Serializable id) {
      throw new AssertionFailure(
          "ProxyInstantiator can only be used to instantiate component");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInstance(Object object) {
      return componentContract.isInstance(object);
    }
  }
}
