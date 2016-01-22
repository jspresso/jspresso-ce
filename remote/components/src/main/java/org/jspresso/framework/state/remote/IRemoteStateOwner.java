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
 * Implemented by classes that are remote value state aware.
 *
 * @author Vincent Vandenschrick
 */
public interface IRemoteStateOwner {

  /**
   * Gets the actual state owner value that may be different from the state
   * value.
   *
   * @return the actual state owner value.
   */
  Object actualValue();

  /**
   * Extracts the state from the state owner.
   *
   * @return the state value object.
   */
  RemoteValueState getState();

  /**
   * Triggers synchronization of the remote state.
   */
  void synchRemoteState();

  /**
   * Sets the actual owner value from state value, allowing for a transformation
   * if necessary.
   *
   * @param stateValue
   *          the incoming state value.
   */
  void setValueFromState(Object stateValue);

  /**
   * Sets the remoteStateValueMapper.
   *
   * @param remoteStateValueMapper
   *          the remoteStateValueMapper to set.
   */
  void setRemoteStateValueMapper(IRemoteStateValueMapper remoteStateValueMapper);
}
