/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.projection;

import org.apache.commons.lang.ObjectUtils;

import com.d2s.framework.view.descriptor.projection.IChildProjectionViewDescriptor;

/**
 * A child projection is a non-root projection (it has a parent). A child
 * projection uses a view to render its projected artifact.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ChildProjection extends Projection {

  private Projection                     parent;
  private IChildProjectionViewDescriptor viewDescriptor;

  /**
   * Gets the projection's parent projection.
   * 
   * @return the parent projection or null if none.
   */
  public Projection getParent() {
    return parent;
  }

  /**
   * Sets the parent projection. It will fire a "parent" property change event.
   * 
   * @param parent
   *          the parent projection to set or null if none.
   */
  public void setParent(Projection parent) {
    if (ObjectUtils.equals(this.parent, parent)) {
      return;
    }
    Projection oldParent = getParent();
    if (getParent() != null) {
      getParent().removeChild(this);
    }
    this.parent = parent;
    if (getParent() != null && !getParent().getChildren().contains(this)) {
      getParent().addChild(this);
    }
    firePropertyChange("parent", oldParent, getParent());
  }

  /**
   * Gets the viewDescriptor.
   * 
   * @return the viewDescriptor.
   */
  public IChildProjectionViewDescriptor getViewDescriptor() {
    return viewDescriptor;
  }

  /**
   * Sets the viewDescriptor.
   * 
   * @param viewDescriptor
   *          the viewDescriptor to set.
   */
  public void setViewDescriptor(
      IChildProjectionViewDescriptor viewDescriptor) {
    this.viewDescriptor = viewDescriptor;
  }
}
