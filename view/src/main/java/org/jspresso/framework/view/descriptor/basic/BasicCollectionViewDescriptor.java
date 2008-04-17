/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor.basic;

import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.view.descriptor.ICollectionViewDescriptor;


/**
 * Default implementation of a collection view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
      iconImageURL = ((ICollectionDescriptorProvider<?>) getModelDescriptor())
          .getCollectionDescriptor().getElementDescriptor().getIconImageURL();
      setIconImageURL(iconImageURL);
    }
    return iconImageURL;
  }
}
