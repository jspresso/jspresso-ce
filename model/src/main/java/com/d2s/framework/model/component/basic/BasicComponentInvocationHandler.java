/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.component.basic;

import java.util.HashMap;
import java.util.Map;

import com.d2s.framework.model.component.IComponent;
import com.d2s.framework.model.component.IComponentCollectionFactory;
import com.d2s.framework.model.component.IComponentExtensionFactory;
import com.d2s.framework.model.component.IComponentFactory;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.util.accessor.IAccessorFactory;

/**
 * This is the core implementation of all inlined components in the application.
 * Instances of this class serve as handlers for proxies representing the
 * inlined components.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicComponentInvocationHandler extends
    AbstractComponentInvocationHandler {

  private static final long   serialVersionUID = -3178070064423598514L;

  private Map<String, Object> properties;

  /**
   * Constructs a new <code>BasiccomponentInvocationHandler</code> instance.
   * 
   * @param componentDescriptor
   *            The descriptor of the proxy component.
   * @param inlineComponentFactory
   *            the factory used to create inline components.
   * @param collectionFactory
   *            The factory used to create empty component collections from
   *            collection getters.
   * @param accessorFactory
   *            The factory used to access proxy properties.
   * @param extensionFactory
   *            The factory used to create component extensions based on their
   *            classes.
   */
  protected BasicComponentInvocationHandler(
      IComponentDescriptor<IComponent> componentDescriptor,
      IComponentFactory inlineComponentFactory,
      IComponentCollectionFactory<IComponent> collectionFactory,
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
    if (proxy == another) {
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected int computeHashCode() {
    return super.hashCode();
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
