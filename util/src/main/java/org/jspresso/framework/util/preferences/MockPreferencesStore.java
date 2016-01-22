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
 * A no-op preference store.
 *
 * @author Vincent Vandenschrick
 */
public class MockPreferencesStore implements IPreferencesStore {

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStorePath(String... storePath) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPreference(String key) {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void putPreference(String key, String value) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePreference(String key) {
    // NO-OP
  }

}
