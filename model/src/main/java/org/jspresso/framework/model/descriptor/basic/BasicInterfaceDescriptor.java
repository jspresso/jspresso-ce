/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.model.descriptor.basic;

/**
 * This descriptor is a mean of factorizing state/behaviour among components,
 * entities or even sub-interfaces. This is a much less coupling mechanism than
 * actual entity inheritance and can be used across entities that don't belong
 * the the same inheritance hierarchy, or even across types (entities,
 * components, interfaces).
 * <p>
 * Please note that interface descriptor is not a way for domain elements to
 * implement arbitrary interfaces coming from external libraries unless they
 * only contain property accessors. The latter can be achieved using service
 * delegates and the {@code serviceDelegates[Bean|Class]Names} property.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete type of components.
 */
public class BasicInterfaceDescriptor<E> extends AbstractComponentDescriptor<E> {

  /**
   * Constructs a new {@code BasicInterfaceDescriptor} instance.
   *
   * @param name
   *          the name of the descriptor which has to be the fully-qualified
   *          class name of its contract.
   */
  public BasicInterfaceDescriptor(String name) {
    super(name);
  }

  /**
   * Gets the entity.
   *
   * @return the entity.
   */
  @Override
  public boolean isEntity() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPurelyAbstract() {
    return true;
  }

  // /**
  // * {@inheritDoc}
  // */
  // @Override
  // public List<IComponentDescriptor<?>> getAncestorDescriptors() {
  // List<IComponentDescriptor<?>> ancestorDescriptors = super
  // .getAncestorDescriptors();
  // if (ancestorDescriptors == null) {
  // ancestorDescriptors = new ArrayList<IComponentDescriptor<?>>(1);
  // }
  // if (!ancestorDescriptors.contains(COMPONENT_DESCRIPTOR)) {
  // ancestorDescriptors.add(COMPONENT_DESCRIPTOR);
  // }
  // return ancestorDescriptors;
  // }
}
