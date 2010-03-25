/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import java.util.Map;

import javax.security.auth.Subject;

import org.jspresso.framework.view.descriptor.ICardNameSelector;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Describes a multi-purpose card view that is configurable with a custom card
 * determination strategy. Cards are registered with a name key that is used to
 * retrieve the card to display based on the card selector selected name key.
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
  public String getCardNameForModel(Object model, Subject subject) {
    if (cardNameSelector != null) {
      return cardNameSelector.getCardNameForModel(model, subject);
    }
    return null;
  }

  /**
   * Configures the card determination strategy. This delegate is responsible
   * for selecting the card name key based on the model bound to the view.
   * Everytime the bound model changes, the card name selector is triggered to
   * select a new card. The names returned by the card name selector must match
   * the names under which the cards are registered. Whenever the card name
   * selector returns an unknown name, the card view displays an empty view.
   * 
   * @param cardNameSelector
   *          the cardNameSelector to set.
   */
  public void setCardNameSelector(ICardNameSelector cardNameSelector) {
    this.cardNameSelector = cardNameSelector;
  }

  /**
   * Registers the card views keyed by their name keys. The names used as key of
   * the <code>Map</code> must match the names that are returned by the
   * registered card name selector.
   * 
   * @param cardViewDescriptors
   *          the cardViewDescriptors to set.
   */
  @Override
  public void setCardViewDescriptors(
      Map<String, IViewDescriptor> cardViewDescriptors) {
    super.setCardViewDescriptors(cardViewDescriptors);
  }
}
