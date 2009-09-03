/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor.basic;

import org.jspresso.framework.view.descriptor.ICardNameSelector;

/**
 * The basic implementation of a card view descriptor.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicCardViewDescriptor extends AbstractCardViewDescriptor {

  private ICardNameSelector cardNameSelector;

  /**
   * Delegates the card name selection to the <code>cardNameSelector</code>.
   * <p>
   * {@inheritDoc}
   */
  public String getCardNameForModel(Object model) {
    if (cardNameSelector != null) {
      return cardNameSelector.getCardNameForModel(model);
    }
    return null;
  }

  /**
   * Sets the cardNameSelector.
   * 
   * @param cardNameSelector
   *          the cardNameSelector to set.
   */
  public void setCardNameSelector(ICardNameSelector cardNameSelector) {
    this.cardNameSelector = cardNameSelector;
  }

}
