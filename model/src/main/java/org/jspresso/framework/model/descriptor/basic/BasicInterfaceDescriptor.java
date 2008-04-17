/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor.basic;

/**
 * Default implementation of an interface component descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the concrete type of components.
 */
public class BasicInterfaceDescriptor<E> extends AbstractComponentDescriptor<E> {

  private boolean computed;

  /**
   * Constructs a new <code>BasicInterfaceDescriptor</code> instance.
   * 
   * @param name
   *            the name of the descriptor which has to be the fully-qualified
   *            class name of its contract.
   */
  public BasicInterfaceDescriptor(String name) {
    super(name);
    this.computed = false;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isComputed() {
    return computed;
  }

  /**
   * Gets the entity.
   * 
   * @return the entity.
   */
  public boolean isEntity() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isPurelyAbstract() {
    return true;
  }

  /**
   * Sets the computed.
   * 
   * @param computed
   *            the computed to set.
   */
  public void setComputed(boolean computed) {
    this.computed = computed;
  }
}
