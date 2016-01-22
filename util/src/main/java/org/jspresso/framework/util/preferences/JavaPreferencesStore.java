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

import java.util.prefs.Preferences;

/**
 * An implementation of preference store relying on the Java preferences API.
 *
 * @author Vincent Vandenschrick
 */
public class JavaPreferencesStore implements IPreferencesStore {

  private Preferences preferences;

  /**
   * Constructs a new {@code JavaPreferencesStore} instance.
   */
  public JavaPreferencesStore() {
    preferences = Preferences.userNodeForPackage(getClass());
  }

  /**
   * Sets the path of this store.
   *
   * @param storePath
   *          the preferences store path.
   */
  @Override
  public void setStorePath(String... storePath) {
    preferences = Preferences.userNodeForPackage(getClass());
    for (String aStorePath : storePath) {
      preferences = preferences.node(aStorePath);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPreference(String key) {
    return preferences.get(key, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void putPreference(String key, String value) {
    preferences.put(key, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePreference(String key) {
    preferences.remove(key);
  }

}
