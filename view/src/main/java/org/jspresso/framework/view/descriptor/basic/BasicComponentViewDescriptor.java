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
package org.jspresso.framework.view.descriptor.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.view.descriptor.ELabelPosition;
import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor;

/**
 * Default implementation of a component view descriptor.
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
 */
public class BasicComponentViewDescriptor extends BasicViewDescriptor implements
    IComponentViewDescriptor {

  private int                           columnCount    = 1;
  private ELabelPosition                labelsPosition = ELabelPosition.ASIDE;
  private List<IPropertyViewDescriptor> propertyViewDescriptors;
  private Map<String, Integer>          propertyWidths;
  private Map<String, List<String>>     renderedChildProperties;

  /**
   * {@inheritDoc}
   */
  public int getColumnCount() {
    return columnCount;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIconImageURL() {
    String iconImageURL = super.getIconImageURL();
    if (iconImageURL == null) {
      iconImageURL = ((IComponentDescriptorProvider<?>) getModelDescriptor())
          .getComponentDescriptor().getIconImageURL();
      setIconImageURL(iconImageURL);
    }
    return iconImageURL;
  }

  /**
   * {@inheritDoc}
   */
  public ELabelPosition getLabelsPosition() {
    return labelsPosition;
  }

  /**
   * {@inheritDoc}
   */
  public List<IPropertyViewDescriptor> getPropertyViewDescriptors() {
    IComponentDescriptor<?> componentDescriptor = ((IComponentDescriptorProvider<?>) getModelDescriptor())
        .getComponentDescriptor();
    if (propertyViewDescriptors == null) {
      List<String> modelRenderedProperties = componentDescriptor
          .getRenderedProperties();
      List<IPropertyViewDescriptor> defaultPropertyViewDescriptors = new ArrayList<IPropertyViewDescriptor>();
      for (String renderedProperty : modelRenderedProperties) {
        IPropertyDescriptor propertyDescriptor = componentDescriptor
            .getPropertyDescriptor(renderedProperty);
        BasicPropertyViewDescriptor propertyViewDescriptor = new BasicPropertyViewDescriptor();
        propertyViewDescriptor.setName(renderedProperty);
        propertyViewDescriptor.setGrantedRoles(propertyDescriptor
            .getGrantedRoles());
        defaultPropertyViewDescriptors.add(propertyViewDescriptor);
      }
      return defaultPropertyViewDescriptors;
    }
    List<IPropertyViewDescriptor> actualPropertyViewDescriptors = new ArrayList<IPropertyViewDescriptor>();
    for (IPropertyViewDescriptor propertyViewDescriptor : propertyViewDescriptors) {
      actualPropertyViewDescriptors.addAll(explodeComponentReferences(
          propertyViewDescriptor,
          (IComponentDescriptorProvider<?>) getModelDescriptor()));
    }
    return actualPropertyViewDescriptors;
  }

  /**
   * Sets the columnCount.
   * 
   * @param columnCount
   *          the columnCount to set.
   */
  public void setColumnCount(int columnCount) {
    this.columnCount = columnCount;
  }

  /**
   * Sets the labelsPosition.
   * 
   * @param labelsPosition
   *          the labelsPosition to set.
   */
  public void setLabelsPosition(ELabelPosition labelsPosition) {
    this.labelsPosition = labelsPosition;
  }

  /**
   * Sets the propertyViewDescriptors.
   * 
   * @param propertyViewDescriptors
   *          the propertyViewDescriptors to set.
   */
  public void setPropertyViewDescriptors(
      List<IPropertyViewDescriptor> propertyViewDescriptors) {
    this.propertyViewDescriptors = propertyViewDescriptors;
  }

  private List<IPropertyViewDescriptor> explodeComponentReferences(
      IPropertyViewDescriptor propertyViewDescriptor,
      IComponentDescriptorProvider<?> componentDescriptorProvider) {
    List<IPropertyViewDescriptor> returnedList = new ArrayList<IPropertyViewDescriptor>();
    IPropertyDescriptor propertyDescriptor = componentDescriptorProvider
        .getComponentDescriptor().getPropertyDescriptor(
            propertyViewDescriptor.getName());
    if ((propertyDescriptor instanceof IReferencePropertyDescriptor<?> && !IEntity.class
        .isAssignableFrom(((IReferencePropertyDescriptor<?>) propertyDescriptor)
            .getReferencedDescriptor().getComponentContract()))) {
      for (String nestedRenderedProperty : ((IReferencePropertyDescriptor<?>) propertyDescriptor)
          .getReferencedDescriptor().getRenderedProperties()) {
        BasicPropertyViewDescriptor nestedPropertyViewDescriptor = new BasicPropertyViewDescriptor();
        nestedPropertyViewDescriptor.setName(propertyDescriptor.getName() + "."
            + nestedRenderedProperty);
        nestedPropertyViewDescriptor.setModelDescriptor(propertyDescriptor);
        nestedPropertyViewDescriptor
            .setWidth(getPropertyWidth(nestedPropertyViewDescriptor.getName()));
        nestedPropertyViewDescriptor
            .setRenderedChildProperties(getRenderedChildProperties(nestedPropertyViewDescriptor
                .getName()));
        if (propertyDescriptor instanceof IReferencePropertyDescriptor<?>) {
          returnedList.addAll(explodeComponentReferences(
              nestedPropertyViewDescriptor,
              (IReferencePropertyDescriptor<?>) propertyDescriptor));
        } else {
          returnedList.add(nestedPropertyViewDescriptor);
        }
      }
    } else {
      if (propertyViewDescriptor instanceof BasicPropertyViewDescriptor) {
        if (propertyViewDescriptor.getModelDescriptor() == null) {
          ((BasicPropertyViewDescriptor) propertyViewDescriptor)
              .setModelDescriptor(propertyDescriptor);
        }
        if (getPropertyWidth(propertyDescriptor.getName()) > 1) {
          ((BasicPropertyViewDescriptor) propertyViewDescriptor)
              .setWidth(getPropertyWidth(propertyDescriptor.getName()));
        }
        if (getRenderedChildProperties(propertyDescriptor.getName()) != null) {
          ((BasicPropertyViewDescriptor) propertyViewDescriptor)
              .setRenderedChildProperties(getRenderedChildProperties(propertyDescriptor
                  .getName()));
        }
      }
      returnedList.add(propertyViewDescriptor);
    }
    return returnedList;
  }

  /**
   * Sets the propertyWidths.
   * 
   * @param propertyWidths
   *          the propertyWidths to set.
   */
  public void setPropertyWidths(Map<String, Object> propertyWidths) {
    this.propertyWidths = new HashMap<String, Integer>();
    for (Map.Entry<String, Object> propertyWidth : propertyWidths.entrySet()) {
      if (propertyWidth.getValue() instanceof String) {
        this.propertyWidths.put(propertyWidth.getKey(), new Integer(
            (String) propertyWidth.getValue()));
      } else {
        this.propertyWidths.put(propertyWidth.getKey(), new Integer(
            ((Number) propertyWidth.getValue()).intValue()));
      }
    }
  }

  private int getPropertyWidth(String propertyName) {
    if (propertyWidths != null) {
      Integer width = propertyWidths.get(propertyName);
      if (width != null) {
        return width.intValue();
      }
    }
    return 1;
  }

  /**
   * Sets the renderedChildProperties.
   * 
   * @param renderedChildProperties
   *          the renderedChildProperties to set.
   */
  public void setRenderedChildProperties(
      Map<String, List<String>> renderedChildProperties) {
    this.renderedChildProperties = renderedChildProperties;
  }

  private List<String> getRenderedChildProperties(String propertyName) {
    List<String> childProperties = null;
    if (renderedChildProperties != null) {
      childProperties = renderedChildProperties.get(propertyName);
    }
    if (childProperties == null) {
      IPropertyDescriptor childPropertyDescriptor = ((IComponentDescriptorProvider<?>) getModelDescriptor())
          .getComponentDescriptor().getPropertyDescriptor(propertyName);
      if (childPropertyDescriptor instanceof ICollectionPropertyDescriptor) {
        return ((ICollectionDescriptor<?>) ((ICollectionPropertyDescriptor<?>) childPropertyDescriptor)
            .getCollectionDescriptor()).getElementDescriptor()
            .getRenderedProperties();
      } else if (childPropertyDescriptor instanceof IReferencePropertyDescriptor) {
        return ((IReferencePropertyDescriptor<?>) childPropertyDescriptor)
            .getReferencedDescriptor().getRenderedProperties();
      }
    }
    return childProperties;
  }
}
