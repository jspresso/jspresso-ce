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
    import org.jspresso.framework.view.descriptor.IBorderViewDescriptor;
    import org.jspresso.framework.view.descriptor.IViewDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.view.descriptor.basic.BasicBorderViewDescriptor")]
    public class BasicBorderViewDescriptor extends BasicCompositeViewDescriptor implements IBorderViewDescriptor {

        private var _centerViewDescriptor:IViewDescriptor;
        private var _eastViewDescriptor:IViewDescriptor;
        private var _northViewDescriptor:IViewDescriptor;
        private var _southViewDescriptor:IViewDescriptor;
        private var _westViewDescriptor:IViewDescriptor;

        public function set centerViewDescriptor(value:IViewDescriptor):void {
            _centerViewDescriptor = value;
        }
        public function get centerViewDescriptor():IViewDescriptor {
            return _centerViewDescriptor;
        }

        public function set eastViewDescriptor(value:IViewDescriptor):void {
            _eastViewDescriptor = value;
        }
        public function get eastViewDescriptor():IViewDescriptor {
            return _eastViewDescriptor;
        }

        public function set northViewDescriptor(value:IViewDescriptor):void {
            _northViewDescriptor = value;
        }
        public function get northViewDescriptor():IViewDescriptor {
            return _northViewDescriptor;
        }

        public function set southViewDescriptor(value:IViewDescriptor):void {
            _southViewDescriptor = value;
        }
        public function get southViewDescriptor():IViewDescriptor {
            return _southViewDescriptor;
        }

        public function set westViewDescriptor(value:IViewDescriptor):void {
            _westViewDescriptor = value;
        }
        public function get westViewDescriptor():IViewDescriptor {
            return _westViewDescriptor;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _centerViewDescriptor = input.readObject() as IViewDescriptor;
            _eastViewDescriptor = input.readObject() as IViewDescriptor;
            _northViewDescriptor = input.readObject() as IViewDescriptor;
            _southViewDescriptor = input.readObject() as IViewDescriptor;
            _westViewDescriptor = input.readObject() as IViewDescriptor;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_centerViewDescriptor);
            output.writeObject(_eastViewDescriptor);
            output.writeObject(_northViewDescriptor);
            output.writeObject(_southViewDescriptor);
            output.writeObject(_westViewDescriptor);
        }
    }
}