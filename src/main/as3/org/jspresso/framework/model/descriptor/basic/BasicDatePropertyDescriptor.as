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


package org.jspresso.framework.model.descriptor.basic {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import org.granite.util.Enum;
    import org.jspresso.framework.model.descriptor.EDateType;
    import org.jspresso.framework.model.descriptor.IDatePropertyDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.model.descriptor.basic.BasicDatePropertyDescriptor")]
    public class BasicDatePropertyDescriptor extends BasicScalarPropertyDescriptor implements IDatePropertyDescriptor {

        private var _type:EDateType;

        public function set type(value:EDateType):void {
            _type = value;
        }
        public function get type():EDateType {
            return _type;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _type = Enum.readEnum(input) as EDateType;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_type);
        }
    }
}