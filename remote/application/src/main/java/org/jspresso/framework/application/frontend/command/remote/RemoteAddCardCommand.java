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
package org.jspresso.framework.application.frontend.command.remote;

import org.jspresso.framework.gui.remote.RComponent;

/**
 * This command is used to add a card in a remote card container.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteAddCardCommand extends RemoteCommand {

  private static final long serialVersionUID = -8530785074677068249L;

  private RComponent        card;
  private String            cardName;

  /**
   * Gets the card.
   *
   * @return the card.
   */
  public RComponent getCard() {
    return card;
  }

  /**
   * Gets the cardName.
   *
   * @return the cardName.
   */
  public String getCardName() {
    return cardName;
  }

  /**
   * Sets the card.
   *
   * @param card
   *          the card to set.
   */
  public void setCard(RComponent card) {
    this.card = card;
  }

  /**
   * Sets the cardName.
   *
   * @param cardName
   *          the cardName to set.
   */
  public void setCardName(String cardName) {
    this.cardName = cardName;
  }

}
