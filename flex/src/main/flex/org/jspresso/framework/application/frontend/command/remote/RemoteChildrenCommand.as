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


package org.jspresso.framework.application.frontend.command.remote {

import mx.collections.ListCollectionView;

[RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand")]
public class RemoteChildrenCommand extends RemoteCommand {

  private var _children:ListCollectionView;
  private var _remove:Boolean;

  public function RemoteChildrenCommand() {
    //default constructor.
  }

  public function set children(value:ListCollectionView):void {
    _children = value;
  }

  public function get children():ListCollectionView {
    return _children;
  }

  public function get remove():Boolean {
    return _remove;
  }

  public function set remove(value:Boolean):void {
    _remove = value;
  }

}
}
