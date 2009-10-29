/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
  public String getCardNameForModel(Object model, Subject subject) {
    if (cardNameSelector != null) {
      return cardNameSelector.getCardNameForModel(model, subject);
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

  /**
   * Sets the childViewDescriptors. Overriden for visibility purpose.
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
