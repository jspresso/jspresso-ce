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

import java.util.Map;

import javax.security.auth.Subject;

import org.jspresso.framework.view.descriptor.ICardNameSelector;
import org.jspresso.framework.view.descriptor.ICardProvider;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * Describes a multi-purpose card view that is configurable with a custom card
 * determination strategy. Cards are registered with a name key that is used to
 * retrieve the card to display based on the card selector selected name key.
 *
 * @author Vincent Vandenschrick
 */
public class BasicCardViewDescriptor extends AbstractCardViewDescriptor {

  private ICardNameSelector cardNameSelector;

  /**
   * Delegates the card name selection to the {@code cardNameSelector}.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public String getCardNameForModel(Object model, Subject subject) {
    if (cardNameSelector != null) {
      return cardNameSelector.getCardNameForModel(model, subject);
    }
    return null;
  }

  /**
   * Configures the card determination strategy. This delegate is responsible
   * for selecting the card name key based on the model bound to the view.
   * Every time the bound model changes, the card name selector is triggered to
   * select a new card. The names returned by the card name selector must match
   * the names under which the cards are registered. Whenever the card name
   * selector returns an unknown name, the card view displays an empty view. The
   * card name selector can optionally implement {@code ICardProvider} in
   * which case, it will be given a chance to create cards dynamically based on
   * their names.
   *
   * @param cardNameSelector
   *          the cardNameSelector to set.
   */
  public void setCardNameSelector(ICardNameSelector cardNameSelector) {
    this.cardNameSelector = cardNameSelector;
  }

  /**
   * Registers the card views keyed by their name keys. The names used as key of
   * the {@code Map} must match the names that are returned by the
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

  /**
   * Delegates to selector if it implements {@code ICardProvider}.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IViewDescriptor getCardViewDescriptor(String cardName) {
    IViewDescriptor cardViewDescriptor = super.getCardViewDescriptor(cardName);
    if (cardViewDescriptor == null) {
      if (cardNameSelector instanceof ICardProvider) {
        cardViewDescriptor = ((ICardProvider) cardNameSelector)
            .getCardViewDescriptor(cardName);
        putCardViewDescriptor(cardName, cardViewDescriptor);
      }
    }
    return cardViewDescriptor;
  }
}
