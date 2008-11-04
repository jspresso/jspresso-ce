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
    import org.jspresso.framework.model.descriptor.IDecimalPropertyDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.model.descriptor.basic.BasicDecimalPropertyDescriptor")]
    public class BasicDecimalPropertyDescriptor extends BasicNumberPropertyDescriptor implements IDecimalPropertyDescriptor {

        private var _maxFractionDigit:Number;

        public function set maxFractionDigit(value:Number):void {
            _maxFractionDigit = value;
        }
        public function get maxFractionDigit():Number {
            return _maxFractionDigit;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _maxFractionDigit = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_maxFractionDigit);
        }
    }
}