/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.persistence.hibernate.entity.tuplizer;

import java.io.Serializable;

import org.hibernate.AssertionFailure;
import org.hibernate.bytecode.BasicProxyFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.mapping.Component;
import org.hibernate.tuple.Instantiator;
import org.hibernate.tuple.component.PojoComponentTuplizer;

/**
 * TODO Comment needed.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ProxyPojoComponentTuplizer extends PojoComponentTuplizer {

  /**
   * Constructs a new <code>ProxyPojoComponentTuplizer</code> instance.
   * 
   * @param component
   */
  public ProxyPojoComponentTuplizer(Component component) {
    super(component);
  }

  protected Instantiator buildInstantiator(Component component) {
    return new ProxyInstantiator(component);
  }

  private static class ProxyInstantiator implements Instantiator {

    private final Class             proxiedClass;
    private final BasicProxyFactory factory;

    public ProxyInstantiator(Component component) {
      proxiedClass = component.getComponentClass();
      if (proxiedClass.isInterface()) {
        factory = Environment.getBytecodeProvider().getProxyFactoryFactory()
            .buildBasicProxyFactory(null, new Class[] {proxiedClass});
      } else {
        factory = Environment.getBytecodeProvider().getProxyFactoryFactory()
            .buildBasicProxyFactory(proxiedClass, null);
      }
    }

    public Object instantiate(Serializable id) {
      throw new AssertionFailure(
          "ProxiedInstantiator can only be used to instantiate component");
    }

    public Object instantiate() {
      return factory.getProxy();
    }

    public boolean isInstance(Object object) {
      return proxiedClass.isInstance(object);
    }
  }
}
