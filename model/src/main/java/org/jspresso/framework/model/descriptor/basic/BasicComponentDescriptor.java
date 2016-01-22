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

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;

/**
 * This type of descriptor is used to describe :
 * <ul>
 * <li>structures that are to be reused but don't have enough focus for being
 * considered as entities. For instance {@code MoneyAmount} component could
 * be composed of a decimal and a reference to a {@code Money} entity. This
 * structure could then be reused in other elements of the domain like an
 * {@code Invoice} or an {@code Article}. Jspresso terminology for
 * these type of structures is <i>&quot;Inline Component&quot;</i>.</li>
 * <li>arbitrary models, that even come from outside of Jspresso (an external
 * library for instance). Describing an arbitrary component allows for seamless
 * usage in the Jspresso view binding architecture. Note that in that case, all
 * behavioural properties like lifecycle interceptors or service delegates are
 * ignored since none of the model behaviour is handled by Jspresso.</li>
 * </ul>
 * Both types of components described above must conform to the <i>Java
 * Beans</i> standard so that its property changes can be followed by the
 * classic {@code add/removePropertyChangeListener} methods since Jspresso
 * binding architecture leverages this behaviour. Jspresso managed components
 * implement it automatically but the developer must ensure it for other types
 * of components.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the concrete type of components.
 */
public class BasicComponentDescriptor<E> extends AbstractComponentDescriptor<E> {

  /**
   * Constructs a new {@code BasicComponentDescriptor} instance.
   */
  public BasicComponentDescriptor() {
    this(null);
  }

  /**
   * Constructs a new {@code BasicComponentDescriptor} instance.
   *
   * @param name
   *          the name of the descriptor which has to be the fully-qualified
   *          class name of its contract.
   */
  public BasicComponentDescriptor(String name) {
    super(name);
  }

  /**
   * Relax ClassNotFoundException since component descriptors can be used
   * without actual java class.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Class<? extends E> getComponentContract() {
    try {
      return super.getComponentContract();
    } catch (RuntimeException ex) {
      if (ex.getCause() instanceof ClassNotFoundException) {
        // might be normal. Surely indicates a component descriptor used without
        // an actual java class.
        return null;
      }
      throw ex;
    }
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
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAncestorDescriptors(
      List<IComponentDescriptor<?>> ancestorDescriptors) {
    List<IComponentDescriptor<?>> refinedAncestorDescriptors = new ArrayList<>();
    if (ancestorDescriptors != null) {
      refinedAncestorDescriptors.addAll(ancestorDescriptors);
    }
    if (!refinedAncestorDescriptors.contains(COMPONENT_DESCRIPTOR)) {
      refinedAncestorDescriptors.add(COMPONENT_DESCRIPTOR);
    }
    super.setAncestorDescriptors(refinedAncestorDescriptors);
  }
}
