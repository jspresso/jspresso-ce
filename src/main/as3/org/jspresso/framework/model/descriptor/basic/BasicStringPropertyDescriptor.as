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
    import org.jspresso.framework.model.descriptor.IStringPropertyDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.model.descriptor.basic.BasicStringPropertyDescriptor")]
    public class BasicStringPropertyDescriptor extends BasicScalarPropertyDescriptor implements IStringPropertyDescriptor {

        private var _maxLength:Number;
        private var _regexpPattern:String;
        private var _regexpPatternSample:String;
        private var _upperCase:Boolean;

        public function set maxLength(value:Number):void {
            _maxLength = value;
        }
        public function get maxLength():Number {
            return _maxLength;
        }

        public function set regexpPattern(value:String):void {
            _regexpPattern = value;
        }
        public function get regexpPattern():String {
            return _regexpPattern;
        }

        public function set regexpPatternSample(value:String):void {
            _regexpPatternSample = value;
        }
        public function get regexpPatternSample():String {
            return _regexpPatternSample;
        }

        public function set upperCase(value:Boolean):void {
            _upperCase = value;
        }
        public function get upperCase():Boolean {
            return _upperCase;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _maxLength = function(o:*):Number { return (o is Number ? o as Number : Number.NaN) } (input.readObject());
            _regexpPattern = input.readObject() as String;
            _regexpPatternSample = input.readObject() as String;
            _upperCase = input.readObject() as Boolean;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_maxLength);
            output.writeObject(_regexpPattern);
            output.writeObject(_regexpPatternSample);
            output.writeObject(_upperCase);
        }
    }
}