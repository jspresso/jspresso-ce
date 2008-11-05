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


package org.jspresso.framework.view.descriptor.basic {

    import flash.utils.IDataInput;
    import flash.utils.IDataOutput;
    import org.jspresso.framework.view.action.ActionMap;
    import org.jspresso.framework.view.descriptor.IViewDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.view.descriptor.basic.BasicViewDescriptor")]
    public class BasicViewDescriptor extends BasicSubviewDescriptor implements IViewDescriptor {

        private var _actionMap:ActionMap;
        private var _background:String;
        private var _borderType:String;
        private var _font:String;
        private var _foreground:String;

        public function set actionMap(value:ActionMap):void {
            _actionMap = value;
        }
        public function get actionMap():ActionMap {
            return _actionMap;
        }

        public function set background(value:String):void {
            _background = value;
        }
        public function get background():String {
            return _background;
        }

        public function set borderType(value:String):void {
            _borderType = value;
        }
        public function get borderType():String {
            return _borderType;
        }

        public function set font(value:String):void {
            _font = value;
        }
        public function get font():String {
            return _font;
        }

        public function set foreground(value:String):void {
            _foreground = value;
        }
        public function get foreground():String {
            return _foreground;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _actionMap = input.readObject() as ActionMap;
            _background = input.readObject() as String;
            _borderType = input.readObject() as String;
            _font = input.readObject() as String;
            _foreground = input.readObject() as String;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_actionMap);
            output.writeObject(_background);
            output.writeObject(_borderType);
            output.writeObject(_font);
            output.writeObject(_foreground);
        }
    }
}