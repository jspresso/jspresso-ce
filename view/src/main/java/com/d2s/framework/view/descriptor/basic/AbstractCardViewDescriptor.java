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
public abstract class AbstractCardViewDescriptor extends BasicViewDescriptor
    implements ICardViewDescriptor {

  private Map<String, IViewDescriptor> cardViewDescriptors;

  /**
   * Gets the childViewDescriptors.
   * 
   * @return the childViewDescriptors.
   */
  public Map<String, IViewDescriptor> getCardViewDescriptors() {
    return cardViewDescriptors;
  }

  /**
   * Sets the childViewDescriptors.
   * 
   * @param cardViewDescriptors
   *          the cardViewDescriptors to set.
   */
  protected void setCardViewDescriptors(
      Map<String, IViewDescriptor> cardViewDescriptors) {
    this.cardViewDescriptors = cardViewDescriptors;
  }
}
