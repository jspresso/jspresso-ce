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
    import mx.collections.ListCollectionView;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.model.descriptor.basic.RangeEnumerationPropertyDescriptor")]
    public class RangeEnumerationPropertyDescriptor extends AbstractEnumerationPropertyDescriptor {

        private var _enumerationValues:ListCollectionView;
        private var _maxValue:Number;
        private var _minValue:Number;
        private var _rangeStep:Number;

        override public function get enumerationValues():ListCollectionView {
            return _enumerationValues;
        }

        public function set maxValue(value:Number):void {
            _maxValue = value;
        }
        public function get maxValue():Number {
            return _maxValue;
        }

        public function set minValue(value:Number):void {
            _minValue = value;
        }
        public function get minValue():Number {
            return _minValue;
        }

        public function set rangeStep(value:Number):void {
            _rangeStep = value;
        }
        public function get rangeStep():Number {
            return _rangeStep;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _enumerationValues = input.readObject() as ListCollectionView;
            _maxValue = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
            _minValue = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
            _rangeStep = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_enumerationValues);
            output.writeObject(_maxValue);
            output.writeObject(_minValue);
            output.writeObject(_rangeStep);
        }
    }
}