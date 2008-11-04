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
    import org.granite.util.Enum;
    import org.jspresso.framework.view.descriptor.EOrientation;
    import org.jspresso.framework.view.descriptor.ISplitViewDescriptor;
    import org.jspresso.framework.view.descriptor.IViewDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.view.descriptor.basic.BasicSplitViewDescriptor")]
    public class BasicSplitViewDescriptor extends BasicCompositeViewDescriptor implements ISplitViewDescriptor {

        private var _leftTopViewDescriptor:IViewDescriptor;
        private var _orientation:EOrientation;
        private var _rightBottomViewDescriptor:IViewDescriptor;

        public function set leftTopViewDescriptor(value:IViewDescriptor):void {
            _leftTopViewDescriptor = value;
        }
        public function get leftTopViewDescriptor():IViewDescriptor {
            return _leftTopViewDescriptor;
        }

        public function set orientation(value:EOrientation):void {
            _orientation = value;
        }
        public function get orientation():EOrientation {
            return _orientation;
        }

        public function set rightBottomViewDescriptor(value:IViewDescriptor):void {
            _rightBottomViewDescriptor = value;
        }
        public function get rightBottomViewDescriptor():IViewDescriptor {
            return _rightBottomViewDescriptor;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _leftTopViewDescriptor = input.readObject() as IViewDescriptor;
            _orientation = Enum.readEnum(input) as EOrientation;
            _rightBottomViewDescriptor = input.readObject() as IViewDescriptor;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_leftTopViewDescriptor);
            output.writeObject(_orientation);
            output.writeObject(_rightBottomViewDescriptor);
        }
    }
}