/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor.basic;

import java.util.ArrayList;
import java.util.List;

import com.d2s.framework.model.component.IComponent;
import com.d2s.framework.model.descriptor.IComponentDescriptor;

/**
 * Default implementation of an inlined component descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the concrete type of components.
 */
public class BasicComponentDescriptor<E> extends AbstractComponentDescriptor<E> {

  private static final IComponentDescriptor<IComponent> COMPONENT_DESCRIPTOR = createComponentDescriptor();

  /**
   * Constructs a new <code>BasicComponentDescriptor</code> instance.
   */
  public BasicComponentDescriptor() {
    this(null);
  }

  /**
   * Constructs a new <code>BasicComponentDescriptor</code> instance.
   * 
   * @param name
   *            the name of the descriptor which has to be the fully-qualified
   *            class name of its contract.
   */
  public BasicComponentDescriptor(String name) {
    super(name);
  }

  private static IComponentDescriptor<IComponent> createComponentDescriptor() {
    BasicInterfaceDescriptor<IComponent> componentDescriptor = new BasicInterfaceDescriptor<IComponent>(
        IComponent.class.getName());

    return componentDescriptor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IComponentDescriptor<?>> getAncestorDescriptors() {
    List<IComponentDescriptor<?>> ancestorDescriptors = super
        .getAncestorDescriptors();
    if (ancestorDescriptors == null) {
      ancestorDescriptors = new ArrayList<IComponentDescriptor<?>>(1);
    }
    if (!ancestorDescriptors.contains(COMPONENT_DESCRIPTOR)) {
      ancestorDescriptors.add(COMPONENT_DESCRIPTOR);
    }
    return ancestorDescriptors;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isComputed() {
    return false;
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
    return false;
  }
}
