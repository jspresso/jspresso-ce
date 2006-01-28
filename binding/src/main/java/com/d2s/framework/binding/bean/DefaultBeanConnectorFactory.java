/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.bean;

import java.util.Collection;

import com.d2s.framework.util.bean.DefaultAccessorFactory;
import com.d2s.framework.util.bean.IAccessorFactory;
import com.d2s.framework.util.bean.IPropertyChangeCapable;
import com.d2s.framework.util.bean.PropertyHelper;

/**
 * Default implementation for BeanConnectors factory.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultBeanConnectorFactory implements IBeanConnectorFactory {

  private IAccessorFactory accessorFactory;

  /**
   * Default BeanConnector factory implementation.
   * <p>
   * {@inheritDoc}
   */
  public BeanConnector createBeanConnector(String id, Class beanClass) {
    return new BeanConnector(id, beanClass, this);
  }

  /**
   * Default BeanCollectionConnector factory implementation.
   * <p>
   * {@inheritDoc}
   */
  public BeanCollectionConnector createBeanCollectionConnector(String id,
      Class beanClass) {
    return new BeanCollectionConnector(id, beanClass, this);
  }

  /**
   * Creates a subclass of IValueConnector depending on the type of the bean
   * property. As of now the created connectors include :
   * <ul>
   * <li><code>BeanRefPropertyConnector</code> in case of a bean reference
   * property.
   * <li><code></code> in case of a bean collection property.
   * <li><code>BeanSimplePropertyConnector</code> in all other cases.
   * </ul>
   * <p>
   * {@inheritDoc}
   */
  public BeanPropertyConnector createBeanPropertyConnector(String property,
      Class beanClass) {
    if (accessorFactory == null) {
      accessorFactory = new DefaultAccessorFactory();
    }
    Class propertyType = getPropertyType(beanClass, property);

    if (IPropertyChangeCapable.class.isAssignableFrom(propertyType)) {
      return new BeanRefPropertyConnector(property, propertyType, this);
    }
    if (Collection.class.isAssignableFrom(propertyType)) {
      // FIXME elementClass should be determined.
      Class elementClass = null;
      // propertyType.get
      return new BeanCollectionPropertyConnector(property, elementClass, this);
    }
    return new BeanSimplePropertyConnector(property, accessorFactory);
  }

  /**
   * Sets the factory for the accessors used to access the bean properties.
   * 
   * @param accessorFactory
   *          The <code>IAccessorFactory</code> to use.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * It return by default a <code>DefaultAccessorFactory</code> unless another
   * one has been set using <code>setAccessorFactory</code>.
   * <p>
   * {@inheritDoc}
   */
  public IAccessorFactory getAccessorFactory() {
    return accessorFactory;
  }

  private Class getPropertyType(Class beanClass, String property) {
    return PropertyHelper.getPropertyType(beanClass, property);
  }
}
