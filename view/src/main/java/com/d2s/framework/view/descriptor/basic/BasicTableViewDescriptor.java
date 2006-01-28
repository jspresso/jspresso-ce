/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.List;

import com.d2s.framework.model.descriptor.ICollectionDescriptorProvider;
import com.d2s.framework.view.descriptor.ITableViewDescriptor;

/**
 * Default implementation of a table view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicTableViewDescriptor extends BasicCollectionViewDescriptor
    implements ITableViewDescriptor {

  private List<String> renderedProperties;

  /**
   * {@inheritDoc}
   */
  public List<String> getRenderedProperties() {
    if (renderedProperties == null) {
      return ((ICollectionDescriptorProvider) getModelDescriptor())
          .getCollectionDescriptor().getElementDescriptor()
          .getRenderedProperties();
    }
    return renderedProperties;
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
}
