/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.d2s.framework.model.descriptor.ICollectionDescriptor;
import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptorProvider;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.view.descriptor.IComponentViewDescriptor;
import com.d2s.framework.view.descriptor.ISubViewDescriptor;

/**
 * Default implementation of a component view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicComponentViewDescriptor extends BasicViewDescriptor implements
    IComponentViewDescriptor {

  private int                       columnCount    = 1;
  private int                       labelsPosition = ASIDE;
  private List<ISubViewDescriptor>  propertyViewDescriptors;
  private Map<String, Integer>      propertyWidths;
  private Map<String, List<String>> renderedChildProperties;

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
  public int getLabelsPosition() {
    return labelsPosition;
  }

  /**
   * {@inheritDoc}
   */
  public List<ISubViewDescriptor> getPropertyViewDescriptors() {
    if (propertyViewDescriptors == null) {
      IComponentDescriptor<?> componentDescriptor = ((IComponentDescriptorProvider<?>) getModelDescriptor())
          .getComponentDescriptor();
      List<String> modelRenderedProperties = componentDescriptor
          .getRenderedProperties();
      List<ISubViewDescriptor> defaultPropertyViewDescriptors = new ArrayList<ISubViewDescriptor>();
      for (String renderedProperty : modelRenderedProperties) {
        BasicSubviewDescriptor propertyDescriptor = new BasicSubviewDescriptor();
        propertyDescriptor.setName(renderedProperty);
        defaultPropertyViewDescriptors.add(propertyDescriptor);
      }
      return defaultPropertyViewDescriptors;
    }
    return propertyViewDescriptors;
  }

  /**
   * {@inheritDoc}
   */
  public int getPropertyWidth(String propertyName) {
    if (propertyWidths != null) {
      Integer width = propertyWidths.get(propertyName);
      if (width != null) {
        return width.intValue();
      }
    }
    return 1;
  }

  /**
   * {@inheritDoc}
   */
  public List<String> getRenderedChildProperties(String propertyName) {
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

  /**
   * Sets the columnCount.
   * 
   * @param columnCount
   *            the columnCount to set.
   */
  public void setColumnCount(int columnCount) {
    this.columnCount = columnCount;
  }

  /**
   * Sets the labelsPosition.
   * 
   * @param labelsPosition
   *            the labelsPosition to set.
   */
  public void setLabelsPosition(int labelsPosition) {
    this.labelsPosition = labelsPosition;
  }

  /**
   * Sets the propertyViewDescriptors.
   * 
   * @param propertyViewDescriptors
   *            the propertyViewDescriptors to set.
   */
  public void setPropertyViewDescriptors(
      List<ISubViewDescriptor> propertyViewDescriptors) {
    this.propertyViewDescriptors = propertyViewDescriptors;
  }

  /**
   * Sets the propertyWidths.
   * 
   * @param propertyWidths
   *            the propertyWidths to set.
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

  /**
   * Sets the renderedChildProperties.
   * 
   * @param renderedChildProperties
   *            the renderedChildProperties to set.
   */
  public void setRenderedChildProperties(
      Map<String, List<String>> renderedChildProperties) {
    this.renderedChildProperties = renderedChildProperties;
  }
}
