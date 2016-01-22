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
package org.jspresso.framework.util.event;

/**
 * This public interface has to be implemented by classes wishing to keep track
 * of of value modifications. This is typically used when binding two connectors
 * together (in a MVC relationship for instance) where the 2 connectors will
 * listen for each other value.
 *
 * @author Vincent Vandenschrick
 */
public interface IValueChangeListener {

  /**
   * This method is triggered whenever a source detects that its value changed.
   * Ideally this method will only fire when {@code oldValue} differs from
   * {@code newValue}.
   *
   * @param evt
   *          The event representing the change. This event will have : <li>
   *          {@code source} set to the source which initiated the event.
   *          <li>{@code oldValue} set to the old value of the source. <li>
   *          {@code newValue} set to the new value of the source.
   */
  void valueChange(ValueChangeEvent evt);

}
