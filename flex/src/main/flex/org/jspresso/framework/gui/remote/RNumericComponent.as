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

		
    [RemoteClass(alias="org.jspresso.framework.gui.remote.RNumericComponent")]
    public class RNumericComponent extends RComponent {

        private var _maxValue:Number;
        private var _minValue:Number;

        public function RNumericComponent() {
          //default constructor.
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
    }
}