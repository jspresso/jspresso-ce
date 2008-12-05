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

		
    [RemoteClass(alias="org.jspresso.framework.gui.remote.RForm")]
    public class RForm extends RContainer {

        private var _columnCount:int;
        private var _elementWidths:Array;
        private var _elements:Array;
        private var _labelsPosition:String;

        public function set columnCount(value:int):void {
            _columnCount = value;
        }
        public function get columnCount():int {
            return _columnCount;
        }

        public function set elementWidths(value:Array):void {
            _elementWidths = value;
        }
        public function get elementWidths():Array {
            return _elementWidths;
        }

        public function set elements(value:Array):void {
            _elements = value;
        }
        public function get elements():Array {
            return _elements;
        }

        public function set labelsPosition(value:String):void {
            _labelsPosition = value;
        }
        public function get labelsPosition():String {
            return _labelsPosition;
        }
    }
}