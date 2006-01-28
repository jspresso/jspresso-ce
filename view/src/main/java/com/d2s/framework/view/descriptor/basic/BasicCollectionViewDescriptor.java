/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import com.d2s.framework.model.descriptor.ICollectionDescriptorProvider;
import com.d2s.framework.view.descriptor.ICollectionViewDescriptor;

/**
 * Default implementation of a collection view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BasicCollectionViewDescriptor extends BasicViewDescriptor
    implements ICollectionViewDescriptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIconImageURL() {
    String iconImageURL = super.getIconImageURL();
    if (iconImageURL == null) {
      iconImageURL = ((ICollectionDescriptorProvider) getModelDescriptor())
          .getCollectionDescriptor().getElementDescriptor().getIconImageURL();
    }
    return iconImageURL;
  }
}
