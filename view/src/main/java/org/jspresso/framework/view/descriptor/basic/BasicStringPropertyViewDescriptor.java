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

import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.IStringPropertyViewDescriptor;

/**
 * This type of view descriptor is used to display a a string property.
 * The objective is to be able to configure an action bound to character typing.
 *
 * @author Vincent Vandenschrick
 */
public class BasicStringPropertyViewDescriptor extends BasicPropertyViewDescriptor
    implements IStringPropertyViewDescriptor {

  private IDisplayableAction characterAction;

  /**
   * Gets character action.
   *
   * @return the character action
   */
  @Override
  public IDisplayableAction getCharacterAction() {
    return characterAction;
  }

  /**
   * Configures an action that gets triggered every time the text is changed in the field.
   *
   * @param characterAction the character action
   */
  public void setCharacterAction(IDisplayableAction characterAction) {
    this.characterAction = characterAction;
  }
}
