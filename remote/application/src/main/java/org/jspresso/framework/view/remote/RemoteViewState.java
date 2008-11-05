/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view.remote;

import org.jspresso.framework.binding.remote.state.RemoteValueState;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.view.descriptor.IViewDescriptor;

/**
 * A view state that can be sent to the client.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteViewState {

  private RemoteValueState valueState;
  private IViewDescriptor  descriptor;
  private RComponent       peer;
  
  /**
   * Gets the valueState.
   * 
   * @return the valueState.
   */
  public RemoteValueState getValueState() {
    return valueState;
  }
  
  /**
   * Sets the valueState.
   * 
   * @param valueState the valueState to set.
   */
  public void setValueState(RemoteValueState valueState) {
    this.valueState = valueState;
  }
  
  /**
   * Gets the descriptor.
   * 
   * @return the descriptor.
   */
  public IViewDescriptor getDescriptor() {
    return descriptor;
  }
  
  /**
   * Sets the descriptor.
   * 
   * @param descriptor the descriptor to set.
   */
  public void setDescriptor(IViewDescriptor descriptor) {
    this.descriptor = descriptor;
  }
  
  /**
   * Gets the peer.
   * 
   * @return the peer.
   */
  public RComponent getPeer() {
    return peer;
  }
  
  /**
   * Sets the peer.
   * 
   * @param peer the peer to set.
   */
  public void setPeer(RComponent peer) {
    this.peer = peer;
  }

}
