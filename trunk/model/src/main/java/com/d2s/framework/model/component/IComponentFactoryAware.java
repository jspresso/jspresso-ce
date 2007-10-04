/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.component;

/**
 * Implemented by objects which have a insterest in componentFactory.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IComponentFactoryAware {

  /**
   * Sets the component factory.
   * 
   * @param componentFactory
   *            the component factory to set.
   */
  void setComponentFactory(IComponentFactory componentFactory);
}
