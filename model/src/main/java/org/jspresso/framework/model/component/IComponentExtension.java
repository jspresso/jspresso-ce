/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.component;

/**
 * This is an interface used to identify classes responsible for providing
 * component accessors which are not directly related to the core properties.
 * Such classes provide derived (computed) properties of the component.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <T>
 *            The class of the extended component.
 */
public interface IComponentExtension<T extends IComponent> {

  /**
   * Returns the component instance to which this extension is attached.
   * 
   * @return The extended entity instance.
   */
  T getComponent();
}
