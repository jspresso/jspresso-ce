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
package org.jspresso.framework.util.scripting;

/**
 * Interface implemented by ny scriptable object.
 *
 * @author Vincent Vandenschrick
 */
public interface IScript {

  /**
   * {@code CONTEXT} is the script execution context map.
   */
  String CONTEXT         = "CONTEXT";

  /**
   * {@code SCRIPTED_OBJECT} is the script context key of the scripted
   * object (the object the script is working on).
   */
  String SCRIPTED_OBJECT = "SCRIPTED_OBJECT";

  /**
   * Gets the scripting language used.
   *
   * @return the scripting language used.
   */
  String getLanguage();

  /**
   * Gets the script code.
   *
   * @return the script code.
   */
  String getScript();

  /**
   * Gets the scripted object.
   *
   * @return the scripted object.
   */
  Object getScriptedObject();
}
