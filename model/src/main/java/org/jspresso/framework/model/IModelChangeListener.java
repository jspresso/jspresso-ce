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
package org.jspresso.framework.model;

/**
 * This public interface must be implemented by classes willing to keep track of
 * {@code IModelProvider} model changes.
 *
 * @author Vincent Vandenschrick
 */
public interface IModelChangeListener {

  /**
   * This method is triggered when the listener detects that a
   * {@code IModelProvider} model changed. Ideally this method will only
   * fire when {@code oldValue} differs from {@code newValue}.
   *
   * @param evt
   *          The event representing the change. This event will have : <li>
   *          {@code source} set to the bean provider which first initiated
   *          the event. <li>{@code oldValue} set to the new source's
   *          model. <li>{@code newValue} set to the old source's model.
   */
  void modelChange(ModelChangeEvent evt);

}
