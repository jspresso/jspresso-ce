/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.component;

/**
 * This interface establishes the contract implemented by component extension
 * factories. A component extension is a delegate instance attached to an
 * component instance and responsible for providing access on computed
 * properties.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IComponentExtensionFactory {

  /**
   * Constructs a new extension instance. Component extension classes must
   * implement constructors in the form of
   * <code>public ComponentExtension(ComponentContract component)</code>.
   *
   * @param <E>
   *          the real component type.
   * @param extensionClass
   *          The class of the component extension.
   * @param componentContract
   *          The interface of the component.
   * @param component
   *          the component instance this extension will be attached to.
   * @return The constructed component extension.
   */
  <E extends IComponent> IComponentExtension<E> createComponentExtension(
      Class<IComponentExtension<E>> extensionClass,
      Class<? extends E> componentContract, E component);
}
