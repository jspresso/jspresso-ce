/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.model.descriptor.entity.basic;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.AbstractComponentDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicIntegerPropertyDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicInterfaceDescriptor;
import org.jspresso.framework.model.descriptor.basic.BasicStringPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;

/**
 * Default implementation of entity descriptors.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicEntityDescriptor extends AbstractComponentDescriptor<IEntity> {

  private static final IComponentDescriptor<IEntity> ENTITY_DESCRIPTOR = createEntityDescriptor();

  private boolean                                    purelyAbstract;

  /**
   * Constructs a new <code>BasicEntityDescriptor</code> instance.
   * 
   * @param name
   *          the name of the descriptor which has to be the fully-qualified
   *          class name of its contract.
   */
  public BasicEntityDescriptor(String name) {
    super(name);
    this.purelyAbstract = false;
  }

  private static IComponentDescriptor<IEntity> createEntityDescriptor() {
    BasicInterfaceDescriptor<IEntity> entityDescriptor = new BasicInterfaceDescriptor<IEntity>(
        IEntity.class.getName());

    List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>(
        2);

    BasicStringPropertyDescriptor idPropertyDescriptor = new BasicStringPropertyDescriptor();
    idPropertyDescriptor.setName(IEntity.ID);
    idPropertyDescriptor.setReadOnly(true);
    propertyDescriptors.add(idPropertyDescriptor);

    BasicIntegerPropertyDescriptor versionPropertyDescriptor = new BasicIntegerPropertyDescriptor();
    versionPropertyDescriptor.setName(IEntity.VERSION);
    versionPropertyDescriptor.setReadOnly(true);
    propertyDescriptors.add(versionPropertyDescriptor);

    entityDescriptor.setPropertyDescriptors(propertyDescriptors);

    return entityDescriptor;
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
    if (!ancestorDescriptors.contains(ENTITY_DESCRIPTOR)) {
      ancestorDescriptors.add(ENTITY_DESCRIPTOR);
    }
    return ancestorDescriptors;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getRenderedProperties() {
    List<String> superRenderedProperties = super.getRenderedProperties();
    if (superRenderedProperties != null) {
      superRenderedProperties.remove(IEntity.ID);
      superRenderedProperties.remove(IEntity.VERSION);
    }
    return superRenderedProperties;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isEntity() {
    return true;
  }

  /**
   * Gets the purelyAbstract.
   * 
   * @return the purelyAbstract.
   */
  public boolean isPurelyAbstract() {
    return purelyAbstract;
  }

  /**
   * Sets the purelyAbstract.
   * 
   * @param purelyAbstract
   *          the purelyAbstract to set.
   */
  public void setPurelyAbstract(boolean purelyAbstract) {
    this.purelyAbstract = purelyAbstract;
  }
}
