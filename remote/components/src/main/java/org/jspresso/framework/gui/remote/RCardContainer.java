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
package org.jspresso.framework.gui.remote;

/**
 * A container with stacked children views.
 *
 * @author Vincent Vandenschrick
 */
public class RCardContainer extends RContainer {

  private static final long serialVersionUID = -5099589404498148055L;

  private String[]          cardNames;
  private RComponent[]      cards;

  /**
   * Constructs a new {@code RCardContainer} instance.
   *
   * @param guid
   *          the guid
   */
  public RCardContainer(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RCardContainer} instance. Only used for
   * serialization support.
   */
  public RCardContainer() {
    // For serialization support
  }

  /**
   * Gets the cardNames.
   *
   * @return the cardNames.
   */
  public String[] getCardNames() {
    return cardNames;
  }

  /**
   * Gets the cards.
   *
   * @return the cards.
   */
  public RComponent[] getCards() {
    return cards;
  }

  /**
   * Sets the cardNames.
   *
   * @param cardNames
   *          the cardNames to set.
   */
  public void setCardNames(String... cardNames) {
    this.cardNames = cardNames;
  }

  /**
   * Sets the cards.
   *
   * @param cards
   *          the cards to set.
   */
  public void setCards(RComponent... cards) {
    this.cards = cards;
  }
}
