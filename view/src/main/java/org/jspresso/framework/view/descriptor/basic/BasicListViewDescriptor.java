/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor.basic;

import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.view.descriptor.IListViewDescriptor;


/**
 * Default implementation of a list view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
