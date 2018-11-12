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
package org.jspresso.framework.view.descriptor.mobile;

import java.util.List;

import javax.security.auth.Subject;

import org.springframework.beans.BeanUtils;

import org.jspresso.framework.util.gui.EPosition;
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.PropertyCardViewDescriptor;

/**
 * Describes a model-bound card view that is configurable with a model property used to determine the card yo display.
 *
 * @author Vincent Vandenschrick
 */
public class MobilePropertyCardViewDescriptor extends PropertyCardViewDescriptor implements IMobileViewDescriptor {

  private EPosition    position;
  private List<String> forClientTypes;

  /**
   * Instantiates a new Mobile card view descriptor.
   */
  public MobilePropertyCardViewDescriptor() {
    this.position = EPosition.LEFT;
  }

  /**
   * Gets  position.
   *
   * @return the position
   */
  @Override
  public EPosition getPosition() {
    return position;
  }

  /**
   * Sets  position.
   *
   * @param position
   *     the  position
   */
  public void setPosition(EPosition position) {
    this.position = position;
  }


  /**
   * Gets for client types.
   *
   * @return the for client types
   */
  @Override
  public List<String> getForClientTypes() {
    return forClientTypes;
  }

  /**
   * Sets for client types.
   *
   * @param forClientTypes
   *     the for client types
   */
  public void setForClientTypes(List<String> forClientTypes) {
    this.forClientTypes = forClientTypes;
  }

  /**
   * Clone read only mobile card view descriptor.
   *
   * @return the mobile card view descriptor
   */
  @Override
  public synchronized MobilePropertyCardViewDescriptor cloneReadOnly() {
    if (readOnlyClone == null) {
      readOnlyClone = new MobilePropertyCardViewDescriptor() {
        @Override
        public IViewDescriptor getCardViewDescriptor(String cardName) {
          IViewDescriptor cardViewDescriptor = super.getCardViewDescriptor(cardName);
          if (cardViewDescriptor == null) {
            IViewDescriptor delegate = MobilePropertyCardViewDescriptor.this.getCardViewDescriptor(cardName);
            if (delegate != null) {
              cardViewDescriptor = (IViewDescriptor) delegate.cloneReadOnly();
            }
            putCardViewDescriptor(cardName, cardViewDescriptor);
          }
          return cardViewDescriptor;
        }

        @Override
        public String getCardNameForModel(Object model, Subject subject) {
          return MobilePropertyCardViewDescriptor.this.getCardNameForModel(model, subject);
        }
      };
      BeanUtils.copyProperties(this, readOnlyClone);
    }
    return (MobilePropertyCardViewDescriptor) readOnlyClone;
  }
}
