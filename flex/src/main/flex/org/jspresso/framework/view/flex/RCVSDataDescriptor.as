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

package org.jspresso.framework.view.flex {

import mx.collections.ListCollectionView;
import mx.controls.treeClasses.DefaultDataDescriptor;

import org.jspresso.framework.state.remote.RemoteCompositeValueState;

public class RCVSDataDescriptor extends DefaultDataDescriptor {

  public function RCVSDataDescriptor() {
    super();
  }

  public override function isBranch(node:Object, model:Object = null):Boolean {
    if (node is RemoteCompositeValueState) {
      var children:ListCollectionView = (node as RemoteCompositeValueState).children;
      return children && children.length > 0;

    }
    return super.isBranch(node, model);
  }
}
}
