/**
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
 */


package org.jspresso.framework.gui.remote {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import org.granite.collections.IMap;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.gui.remote.RContainer")]
    public class RContainer extends RComponent {

        private var _children:IMap;

        public function set children(value:IMap):void {
            _children = value;
        }
        public function get children():IMap {
            return _children;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _children = input.readObject() as IMap;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_children);
        }
    }
}