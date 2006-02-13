/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.List;
import java.util.Map;

import com.d2s.framework.model.descriptor.ICollectionPropertyDescriptor;
import com.d2s.framework.model.descriptor.IComponentDescriptor;
import com.d2s.framework.model.descriptor.IPropertyDescriptor;
import com.d2s.framework.model.descriptor.IReferencePropertyDescriptor;
import com.d2s.framework.view.descriptor.IComponentViewDescriptor;

/**
 * Default implementation of a component view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicComponentViewDescriptor extends BasicViewDescriptor implements
    IComponentViewDescriptor {

  private List<String>              renderedProperties;
  private int                       labelsPosition = ABOVE;
  private int                       columnCount    = 1;
  private Map<String, Integer>      propertyWidths;
  private Map<String, List<String>> renderedChildProperties;

  /**
   * {@inheritDoc}
   */
  public List<String> getRenderedProperties() {
    if (renderedProperties == null) {
      return ((IComponentDescriptor) getModelDescriptor())
          .getRenderedProperties();
    }
    return renderedProperties;
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
  public int getColumnCount() {
    return columnCount;
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
  public void setLabelsPosition(int labelsPosition) {
    this.labelsPosition = labelsPosition;
  }

  /**
   * Sets the propertyWidths.
   * 
   * @param propertyWidths
   *          the propertyWidths to set.
   */
  public void setPropertyWidths(Map<String, Integer> propertyWidths) {
    this.propertyWidths = propertyWidths;
  }

  /**
   * Sets the renderedProperties.
   * 
   * @param renderedProperties
   *          the renderedProperties to set.
   */
  public void setRenderedProperties(List<String> renderedProperties) {
    this.renderedProperties = renderedProperties;
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
      IPropertyDescriptor childPropertyDescriptor = ((IComponentDescriptor) getModelDescriptor())
          .getPropertyDescriptor(propertyName);
      if (childPropertyDescriptor instanceof ICollectionPropertyDescriptor) {
        return ((ICollectionPropertyDescriptor) childPropertyDescriptor)
            .getCollectionDescriptor().getElementDescriptor()
            .getRenderedProperties();
      } else if (childPropertyDescriptor instanceof IReferencePropertyDescriptor) {
        return ((IReferencePropertyDescriptor) childPropertyDescriptor)
            .getReferencedDescriptor().getRenderedProperties();
      }
    }
    return childProperties;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIconImageURL() {
    String iconImageURL = super.getIconImageURL();
    if (iconImageURL == null) {
      iconImageURL = ((IComponentDescriptor) getModelDescriptor())
          .getIconImageURL();
    }
    return iconImageURL;
  }
}
