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
package org.jspresso.framework.state.remote;

/**
 * The factory interface for remote value states.
 *
 * @author Vincent Vandenschrick
 */
public interface IRemoteValueStateFactory {

  /**
   * Creates a new {@code RemoteCompositeValueState} instance.
   *
   * @param guid
   *          the state guid.
   * @param permId
   *          the seed to generate permanent ids.
   * @return the created {@code RemoteValueState}.
   */
  RemoteCompositeValueState createRemoteCompositeValueState(String guid,
      String permId);

  /**
   * Creates a new {@code RemoteValueState} instance.
   *
   * @param guid
   *          the state guid.
   * @param permId
   *          the seed to generate permanent ids.
   * @return the created {@code RemoteValueState}.
   */
  RemoteValueState createRemoteValueState(String guid, String permId);

  /**
   * Creates a new {@code RemoteFormattedValueState} instance.
   *
   * @param guid
   *          the state guid.
   * @param permId
   *          the seed to generate permanent ids.
   * @return the created {@code RemoteFormattedValueState}.
   */
  RemoteFormattedValueState createRemoteFormattedValueState(String guid,
      String permId);

}
