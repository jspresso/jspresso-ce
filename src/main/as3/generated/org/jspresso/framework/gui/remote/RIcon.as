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

    import org.jspresso.framework.util.remote.RemotePeer;

    [RemoteClass(alias="org.jspresso.framework.gui.remote.RIcon")]
    public class RIcon extends RemotePeer {

        private var _height:int;
        private var _imageUrlSpec:String;
        private var _width:int;

        public function set height(value:int):void {
            _height = value;
        }
        public function get height():int {
            return _height;
        }

        public function set imageUrlSpec(value:String):void {
            _imageUrlSpec = value;
        }
        public function get imageUrlSpec():String {
            return _imageUrlSpec;
        }

        public function set width(value:int):void {
            _width = value;
        }
        public function get width():int {
            return _width;
        }
    }
}