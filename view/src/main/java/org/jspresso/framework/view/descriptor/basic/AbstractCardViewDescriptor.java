/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.view.descriptor.basic;

import java.util.HashMap;
import java.util.Map;

import org.jspresso.framework.view.descriptor.ICardViewDescriptor;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * This descriptor serves as abstract base implementation for card view
 * descriptor. A card view is a view stack made of children views (the cards)
 * where only the view (card) at the top of the stack is visible. The actual
 * child view to place on the top of the stack is dynamically determined based
 * on the bound model. This card determination strategy depends on the concrete
 * descriptor sub-types.
 * <p>
 * One might wonder why a card view is not considered as (and actually does not
 * inherit from) a composite view. The difference is that composite views are
 * used aggregate views that displays - hopefully - different parts (the children
 * views) of the <b>same</b> model. A card view descriptor is rather used to
 * make the same UI region display different views depending on different models
 * (or different model states). Once the model is fixed, the card view behaves
 * exactly as its top card.
 * <p>
 * One of the most important usage of card views is when it is combine as the
 * detail in a master-detail view. The detail view may then change dynamically
 * based on the selected master.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractCardViewDescriptor extends BasicViewDescriptor
    implements ICardViewDescriptor {

  private Map<String, IViewDescriptor> cardViewDescriptors;

  /**
   * Gets the child view registered in the card view by its name.
   *
   * @param cardName
   *          the card name to lookup the view for.
   * @return the child view descriptor.
   */
  @Override
  public IViewDescriptor getCardViewDescriptor(String cardName) {
    if (cardViewDescriptors == null) {
      cardViewDescriptors = new HashMap<>();
    }
    return cardViewDescriptors.get(cardName);
  }

  /**
   * Puts the child view registered in the card view by its name.
   *
   * @param cardName
   *          the card name to lookup the view for.
   * @param cardViewDescriptor
   *          the child view descriptor.
   */
  protected void putCardViewDescriptor(String cardName,
      IViewDescriptor cardViewDescriptor) {
    if (cardViewDescriptors == null) {
      cardViewDescriptors = new HashMap<>();
    }
    cardViewDescriptors.put(cardName, cardViewDescriptor);
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
