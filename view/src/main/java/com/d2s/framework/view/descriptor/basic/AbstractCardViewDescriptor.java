/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.Map;

import com.d2s.framework.view.descriptor.ICardViewDescriptor;
import com.d2s.framework.view.descriptor.IViewDescriptor;

/**
 * Abstract root implementation of a card view descriptor.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractCardViewDescriptor extends
    BasicCompositeViewDescriptor implements ICardViewDescriptor {

  private Map<String, IViewDescriptor> viewDescriptors;

  /**
   * Sets the childViewDescriptors.
   * 
   * @param viewDescriptors
   *          the viewDescriptors to set.
   */
  public void setViewDescriptors(Map<String, IViewDescriptor> viewDescriptors) {
    this.viewDescriptors = viewDescriptors;
  }

  /**
   * Gets the childViewDescriptors.
   * 
   * @return the childViewDescriptors.
   */
  public Map<String, IViewDescriptor> getChildViewDescriptors() {
    return viewDescriptors;
  }
}
