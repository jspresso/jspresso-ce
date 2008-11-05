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


package org.jspresso.framework.util.remote {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import flash.utils.IExternalizable;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.util.remote.RemoteServerPeer")]
    public class RemoteClientPeer implements IExternalizable, IRemoteClientPeer {

        private var _uid:String;

        public function get uid():String {
            return _uid;
        }

        public function readExternal(input:IDataInput):void {
            _uid = input.readObject() as String;
        }

        public function writeExternal(output:IDataOutput):void {
            output.writeObject(_uid);
        }
    }
}