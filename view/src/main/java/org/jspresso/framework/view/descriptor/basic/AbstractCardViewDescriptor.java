/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor.basic;

import java.util.Map;

import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;


/**
 * Abstract root implementation of a card view descriptor.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
   *            the cardViewDescriptors to set.
   */
  protected void setCardViewDescriptors(
      Map<String, IViewDescriptor> cardViewDescriptors) {
    this.cardViewDescriptors = cardViewDescriptors;
  }
}
