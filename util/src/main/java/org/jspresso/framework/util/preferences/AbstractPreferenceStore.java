/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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

import java.util.HashMap;
import java.util.Map;

/**
 * Skeleton implementation of a preference store.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractPreferenceStore implements IPreferencesStore {

  private Map<String, String> preferences;

  /**
   * {@inheritDoc}
   */
  public synchronized String getPreference(String key) {
    if (preferences == null) {
      read();
    }
    return preferences.get(key);
  }

  /**
   * {@inheritDoc}
   */
  public synchronized void putPreference(String key, String value) {
    if (preferences == null) {
      read();
    }
    preferences.put(key, value);
  }

  /**
   * {@inheritDoc}
   */
  public synchronized void removePreference(String key) {
    if (preferences == null) {
      read();
    }
    preferences.remove(key);
  }

  /**
   * {@inheritDoc}
   */
  public synchronized void read() {
    preferences = doRead();
    if (preferences == null) {
      preferences = new HashMap<String, String>();
    }
  }

  /**
   * {@inheritDoc}
   */
  public void write() {
    doWrite(preferences);
  }

  /**
   * This method is to be implemented by concrete sub-classes and preforms
   * actual reading of preferences into a map.
   * 
   * @return a map of preferences read from the store.
   */
  protected abstract Map<String, String> doRead();

  /**
   * This method is to be implemented by concrete sub-classes and preforms
   * actual flush of preferences stored into a map.
   * 
   * @param prefs
   *          a map of the store preferences.
   */
  protected abstract void doWrite(Map<String, String> prefs);
}
