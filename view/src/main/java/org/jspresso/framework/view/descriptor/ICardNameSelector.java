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
package org.jspresso.framework.view.descriptor;

import javax.security.auth.Subject;

/**
 * This interface is implemented by strategies determining a card name based on
 * a model instance.
 *
 * @author Vincent Vandenschrick
 */
public interface ICardNameSelector {

  /**
   * Gets the card name to use to present the model.
   *
   * @param model
   *          the model to back the view.
   * @param subject
   *          the JAAS subject.
   * @return the card name to look up the view.
   */
  String getCardNameForModel(Object model, Subject subject);

}
