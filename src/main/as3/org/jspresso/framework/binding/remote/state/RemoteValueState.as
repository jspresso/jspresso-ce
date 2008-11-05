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


package org.jspresso.framework.binding.remote.state {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import org.jspresso.framework.util.remote.RemotePeer;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.binding.remote.state.RemoteValueState")]
    public class RemoteValueState extends RemotePeer {

        private var _readable:Boolean;
        private var _value:Object;
        private var _writable:Boolean;

        public function set readable(value:Boolean):void {
            _readable = value;
        }
        public function get readable():Boolean {
            return _readable;
        }

        public function set value(value:Object):void {
            _value = value;
        }
        public function get value():Object {
            return _value;
        }

        public function set writable(value:Boolean):void {
            _writable = value;
        }
        public function get writable():Boolean {
            return _writable;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _readable = input.readObject() as Boolean;
            _value = input.readObject() as Object;
            _writable = input.readObject() as Boolean;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_readable);
            output.writeObject(_value);
            output.writeObject(_writable);
        }
    }
}