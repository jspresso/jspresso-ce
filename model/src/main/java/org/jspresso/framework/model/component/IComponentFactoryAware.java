/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.component;

/**
 * Implemented by objects which have a insterest in componentFactory.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
