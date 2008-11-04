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


package org.jspresso.framework.view {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import flash.utils.IExternalizable;
    import org.jspresso.framework.binding.IValueConnector;
    import org.jspresso.framework.view.descriptor.IViewDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.view.BasicView")]
    public class BasicView implements IExternalizable, IView {

        private var _connector:IValueConnector;
        private var _descriptor:IViewDescriptor;
        private var _parent:IView;
        private var _peer:Object;

        public function set connector(value:IValueConnector):void {
            _connector = value;
        }
        public function get connector():IValueConnector {
            return _connector;
        }

        public function set descriptor(value:IViewDescriptor):void {
            _descriptor = value;
        }
        public function get descriptor():IViewDescriptor {
            return _descriptor;
        }

        public function set parent(value:IView):void {
            _parent = value;
        }
        public function get parent():IView {
            return _parent;
        }

        public function set peer(value:Object):void {
            _peer = value;
        }
        public function get peer():Object {
            return _peer;
        }

        public function get view():IView {
            return null;
        }

        public function readExternal(input:IDataInput):void {
            _connector = input.readObject() as IValueConnector;
            _descriptor = input.readObject() as IViewDescriptor;
            _parent = input.readObject() as IView;
            _peer = input.readObject() as Object;
        }

        public function writeExternal(output:IDataOutput):void {
            output.writeObject(_connector);
            output.writeObject(_descriptor);
            output.writeObject(_parent);
            output.writeObject(_peer);
        }
    }
}