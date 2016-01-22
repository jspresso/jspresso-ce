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
package org.jspresso.framework.util.preferences;

/**
 * The generic contract for a preferences store.
 *
 * @author Vincent Vandenschrick
 */
public interface IPreferencesStore {

  /**
   * &quot;global&quot; constant.
   */
  String GLOBAL_STORE = "global";

  /**
   * Sets the path of this store.
   *
   * @param storePath
   *          the path identifying this store.
   */
  void setStorePath(String... storePath);

  /**
   * Retrieves a preference from the preference store.
   *
   * @param key
   *          the key of the preference.
   * @return the value of the preference or null if no preference with this key
   *         belongs to this store.
   */
  String getPreference(String key);

  /**
   * Stores a preference into the preference store.
   *
   * @param key
   *          the key of the preference.
   * @param value
   *          the value of the preference or null if no preference with this key
   *          belongs to this store.
   */
  void putPreference(String key, String value);

  /**
   * Removes a preference from the preference store.
   *
   * @param key
   *          the key of the preference.
   */
  void removePreference(String key);
}
