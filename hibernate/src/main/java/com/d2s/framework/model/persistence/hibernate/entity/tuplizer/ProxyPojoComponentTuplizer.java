/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.persistence.hibernate.entity.tuplizer;

import java.io.Serializable;

import org.hibernate.AssertionFailure;
import org.hibernate.mapping.Component;
import org.hibernate.tuple.Instantiator;
import org.hibernate.tuple.component.PojoComponentTuplizer;

import com.d2s.framework.model.component.IComponent;
import com.d2s.framework.model.component.IComponentFactory;

/**
 * A specialized hibernate tuplizer to handle proxy components.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ProxyPojoComponentTuplizer extends PojoComponentTuplizer {
  
  private static final long serialVersionUID = 519439258744322320L;
  private static IComponentFactory inlineComponentFactory;

  /**
   * Constructs a new <code>ProxyPojoComponentTuplizer</code> instance.
   * 
   * @param component
   *            the component to build the proxy for.
   */
  public ProxyPojoComponentTuplizer(Component component) {
    super(component);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Instantiator buildInstantiator(Component component) {
    return new ProxyInstantiator(component);
  }

  private static class ProxyInstantiator implements Instantiator {

    private static final long serialVersionUID = 8746568183869210457L;
    private final Class<? extends IComponent>          componentContract;

    /**
     * Constructs a new <code>ProxyInstantiator</code> instance.
     * 
     * @param component the hibernate component to build this instanciator for.
     */
    @SuppressWarnings("unchecked")
    public ProxyInstantiator(Component component) {
      componentContract = component.getComponentClass();
    }

    /**
     * {@inheritDoc}
     */
    public Object instantiate(@SuppressWarnings("unused")
    Serializable id) {
      throw new AssertionFailure(
          "ProxyInstantiator can only be used to instantiate component");
    }

    /**
     * {@inheritDoc}
     */
    public Object instantiate() {
      return inlineComponentFactory.createComponentInstance(componentContract);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInstance(Object object) {
      return componentContract.isInstance(object);
    }
  }

  
  /**
   * Sets the inlineComponentFactory.
   * 
   * @param inlineComponentFactory the inlineComponentFactory to set.
   */
  public static void setInlineComponentFactory(
      IComponentFactory inlineComponentFactory) {
    ProxyPojoComponentTuplizer.inlineComponentFactory = inlineComponentFactory;
  }
}
