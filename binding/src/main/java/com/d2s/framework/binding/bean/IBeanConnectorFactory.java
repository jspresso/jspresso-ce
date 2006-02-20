/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.bean;

import com.d2s.framework.util.bean.IAccessorFactory;

/**
 * Interface for all factories of bean connector maps.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IBeanConnectorFactory {

  /**
   * Creates a new <code>BeanConnector</code> instance.
   * 
   * @param id
   *          the connector id.
   * @param beanClass
   *          the bean class used by the bean connector.
   * @return the created BeanConnector.
   */
  BeanConnector createBeanConnector(String id, Class beanClass);

  /**
   * Creates a new <code>BeanCollectionConnector</code> instance.
   * 
   * @param id
   *          the connector id.
   * @param elementClass
   *          the collection element class.
   * @return the created BeanCollectionConnector.
   */
  BeanCollectionConnector createBeanCollectionConnector(String id,
      Class elementClass);

  /**
   * Creates a new <code>BeanPropertyConnector</code>. Depending on the type
   * of property, it might create adapted implementations of
   * <code>IValueConnector</code>. For instance, for a bean property which is
   * a bean reference a <code>BeanRefPropertyConnector</code> will be created.
   * 
   * @param property
   *          the java bean property the connector will listen to.
   * @param beanClass
   *          the bean class this connector is used for.
   * @return the created java bean connector.
   */
  BeanPropertyConnector createBeanPropertyConnector(String property,
      Class beanClass);

  /**
   * Gets the <code>IAccessorFactory</code> used.
   * 
   * @return the used accessor factory
   */
  IAccessorFactory getAccessorFactory();
}
