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
package org.jspresso.framework.view.descriptor.basic;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.descriptor.query.EnumQueryStructureDescriptor;
import org.jspresso.framework.model.entity.EntityHelper;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;
import org.jspresso.framework.view.descriptor.IReferencePropertyViewDescriptor;

/**
 * Helper class for property view descriptors.
 *
 * @author Vincent Vandenschrick
 */
public final class PropertyViewDescriptorHelper {

  /**
   * Constructs a new {@code PropertyDescriptorHelper} instance.
   */
  private PropertyViewDescriptorHelper() {
    // Helper class constructor
  }

  /**
   * Explodes component reference property descriptors.
   *
   * @param propertyViewDescriptor
   *          the property view descriptor to explode if necessary.
   * @param componentDescriptorProvider
   *          the component descriptor holding the property descriptor.
   * @return the list of potentially exploded property view descriptors.
   */
  public static List<IPropertyViewDescriptor> explodeComponentReferences(
      IPropertyViewDescriptor propertyViewDescriptor,
      IComponentDescriptorProvider<?> componentDescriptorProvider) {
    List<IPropertyViewDescriptor> returnedList = new ArrayList<>();
    IComponentDescriptor<?> rootComponentDescriptor = componentDescriptorProvider
        .getComponentDescriptor();
    IPropertyDescriptor propertyDescriptor;
    if (propertyViewDescriptor.getModelDescriptor() instanceof IPropertyDescriptor) {
      propertyDescriptor = (IPropertyDescriptor) propertyViewDescriptor
          .getModelDescriptor();
    } else {
      propertyDescriptor = rootComponentDescriptor
          .getPropertyDescriptor(propertyViewDescriptor.getName());
    }
    /*
     * Exclude explicitly configured reference property view filled with a
     * custom LOV action
     */
    boolean toExplode = !(propertyViewDescriptor instanceof IReferencePropertyViewDescriptor)
        || ((IReferencePropertyViewDescriptor) propertyViewDescriptor)
            .getLovAction() == null;

    /*
     * Include inline component reference
     */
    toExplode = toExplode
        && propertyDescriptor instanceof IReferencePropertyDescriptor<?>
        && EntityHelper
            .isInlineComponentReference((IReferencePropertyDescriptor<?>) propertyDescriptor)
        && !(propertyDescriptor instanceof EnumQueryStructureDescriptor);

    if (toExplode) {
      IComponentDescriptor<?> referencedComponentDescriptor = ((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor();
      for (String nestedRenderedProperty : referencedComponentDescriptor
          .getRenderedProperties()) {
        BasicPropertyViewDescriptor nestedPropertyViewDescriptor = new BasicPropertyViewDescriptor();
        nestedPropertyViewDescriptor.setName(propertyDescriptor.getName() + "."
            + nestedRenderedProperty);
        nestedPropertyViewDescriptor.setModelDescriptor(rootComponentDescriptor
            .getPropertyDescriptor(nestedPropertyViewDescriptor.getName()));
        nestedPropertyViewDescriptor.setReadOnly(propertyViewDescriptor
            .isReadOnly());
        nestedPropertyViewDescriptor.setGrantedRoles(propertyViewDescriptor
            .getGrantedRoles());
        nestedPropertyViewDescriptor.setWritabilityGates(propertyViewDescriptor
            .getWritabilityGates());
        nestedPropertyViewDescriptor.setReadabilityGates(propertyViewDescriptor
            .getReadabilityGates());
        nestedPropertyViewDescriptor.setLabelBackground(propertyViewDescriptor
            .getLabelBackground());
        nestedPropertyViewDescriptor.setLabelForeground(propertyViewDescriptor
            .getLabelForeground());
        nestedPropertyViewDescriptor.setLabelFont(propertyViewDescriptor
            .getLabelFont());
        nestedPropertyViewDescriptor.setBackground(propertyViewDescriptor
            .getBackground());
        nestedPropertyViewDescriptor.setForeground(propertyViewDescriptor
            .getForeground());
        nestedPropertyViewDescriptor.setFont(propertyViewDescriptor.getFont());
        returnedList.addAll(explodeComponentReferences(
            nestedPropertyViewDescriptor, componentDescriptorProvider));
      }
    } else {
      if (propertyViewDescriptor.getModelDescriptor() == null
          && propertyViewDescriptor instanceof BasicPropertyViewDescriptor) {
        ((BasicPropertyViewDescriptor) propertyViewDescriptor)
            .setModelDescriptor(propertyDescriptor);
      }
      returnedList.add(propertyViewDescriptor);
    }
    return returnedList;
  }

  /**
   * Explores a property chain and determines whether one of the chain element is
   * computed (thus making the complete chain computed).
   *
   * @param componentDescriptor
   *          the component descriptor from which the property chain is
   *          extracted.
   * @param propertyName
   *          the (potentially nested) property name.
   * @return true if the (potentially nested) property is computed.
   */
  public static boolean isComputed(IComponentDescriptor<?> componentDescriptor,
      String propertyName) {
    String[] propElts = propertyName.split("\\.");
    IComponentDescriptor<?> currentCompDesc = componentDescriptor;
    for (int i = 0; i < propElts.length; i++) {
      IPropertyDescriptor propDesc = currentCompDesc
          .getPropertyDescriptor(propElts[i]);
      if (propDesc.isComputed()) {
        return true;
      }
      if (i < propElts.length - 1) {
        currentCompDesc = ((IReferencePropertyDescriptor<?>) propDesc)
            .getReferencedDescriptor();
      }
    }
    return false;
  }
}
