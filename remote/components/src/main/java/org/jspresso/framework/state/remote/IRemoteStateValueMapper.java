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
 * A simple interface to establish the transformation contract to fill in a
 * remote state value.
 *
 * @author Vincent Vandenschrick
 */
public interface IRemoteStateValueMapper {

  /**
   * Gets the value that has to be set to the remote state when updating it. It
   * should default to the original value but the developer is given a chance
   * here to mutate the actual object returned. This allows for changing the
   * type of objects actually exchanged with the remote frontend peer.
   *
   *
   * @param state the remote value state to map the value for.
   * @param originalValue
   *          the value to fill-in the state with.
   * @return the potentially transformed value.
   */
  Object getValueForState(RemoteValueState state, Object originalValue);

  /**
   * Gets the value that has to be set to the remote connector when updating it.
   * It should default to the original value but the developer is given a
   * chance here to mutate the actual object returned. This allows for changing
   * the type of objects actually exchanged with the remote frontend peer.
   *
   *
   * @param state the remote value state to map the value for.
   * @param originalValue
   *          the value to fill-in the connector with.
   * @return the potentially transformed value.
   */
  Object getValueFromState(@SuppressWarnings("UnusedParameters") RemoteValueState state, Object originalValue);
}
