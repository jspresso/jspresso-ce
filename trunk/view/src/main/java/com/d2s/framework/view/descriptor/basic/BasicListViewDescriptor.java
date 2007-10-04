/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import com.d2s.framework.model.descriptor.ICollectionDescriptorProvider;
import com.d2s.framework.view.descriptor.IListViewDescriptor;

/**
 * Default implementation of a list view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicListViewDescriptor extends BasicCollectionViewDescriptor
    implements IListViewDescriptor {

  private String renderedProperty;

  /**
   * {@inheritDoc}
   */
  public String getRenderedProperty() {
    if (renderedProperty == null) {
      return ((ICollectionDescriptorProvider<?>) getModelDescriptor())
          .getCollectionDescriptor().getElementDescriptor()
          .getToStringProperty();
    }
    return renderedProperty;
  }

  /**
   * Sets the renderedProperty.
   * 
   * @param renderedProperty
   *            the renderedProperty to set.
   */
  public void setRenderedProperty(String renderedProperty) {
    this.renderedProperty = renderedProperty;
  }

}
