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
    import org.jspresso.framework.model.descriptor.IBinaryPropertyDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.model.descriptor.basic.BasicBinaryPropertyDescriptor")]
    public class BasicBinaryPropertyDescriptor extends BasicScalarPropertyDescriptor implements IBinaryPropertyDescriptor {

        private var _fileFilter:Object;
        private var _maxLength:Number;

        public function set fileFilter(value:Object):void {
            _fileFilter = value;
        }
        public function get fileFilter():Object {
            return _fileFilter;
        }

        public function set maxLength(value:Number):void {
            _maxLength = value;
        }
        public function get maxLength():Number {
            return _maxLength;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _fileFilter = input.readObject() as Object;
            _maxLength = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_fileFilter);
            output.writeObject(_maxLength);
        }
    }
}