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


package org.jspresso.framework.gui.remote {

import org.jspresso.framework.state.remote.RemoteCompositeValueState;

[RemoteClass(alias="org.jspresso.framework.gui.remote.RRepeater")]
public class RRepeater extends RCollectionComponent {

  private var _repeated:RComponent;
  private var _viewPrototype:RemoteCompositeValueState;

  public function RRepeater() {
    //default constructor.
  }

  public function get repeated():RComponent {
    return _repeated;
  }

  public function set repeated(value:RComponent):void {
    _repeated = value;
  }


  public function get viewPrototype():RemoteCompositeValueState {
    return _viewPrototype;
  }

  public function set viewPrototype(value:RemoteCompositeValueState):void {
    _viewPrototype = value;
  }

  public override function transferToState(stateMapping:Object):void {
    super.transferToState(stateMapping);
    viewPrototype = viewPrototype ?
        (stateMapping[viewPrototype.guid] ? stateMapping[viewPrototype.guid] as RemoteCompositeValueState :
            viewPrototype) : null;
    if (_repeated) {
      _repeated.transferToState(stateMapping);
    }
  }
}
}
