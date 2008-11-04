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


package org.jspresso.framework.util.descriptor {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import flash.utils.IExternalizable;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.util.descriptor.DefaultDescriptor")]
    public class DefaultDescriptor implements IExternalizable, IDescriptor {

        private var _description:String;
        private var _i18nNameKey:String;
        private var _name:String;

        public function set description(value:String):void {
            _description = value;
        }
        public function get description():String {
            return _description;
        }

        public function set i18nNameKey(value:String):void {
            _i18nNameKey = value;
        }

        public function set name(value:String):void {
            _name = value;
        }
        public function get name():String {
            return _name;
        }

        public function readExternal(input:IDataInput):void {
            _description = input.readObject() as String;
            _i18nNameKey = input.readObject() as String;
            _name = input.readObject() as String;
        }

        public function writeExternal(output:IDataOutput):void {
            output.writeObject(_description);
            output.writeObject(_i18nNameKey);
            output.writeObject(_name);
        }
    }
}