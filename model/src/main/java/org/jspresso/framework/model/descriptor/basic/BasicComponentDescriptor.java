/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;


/**
 * Default implementation of an inlined component descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
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
