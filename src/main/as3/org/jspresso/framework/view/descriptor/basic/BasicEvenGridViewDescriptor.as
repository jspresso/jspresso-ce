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
    
    import mx.collections.ListCollectionView;
    
    import org.jspresso.framework.view.descriptor.IEvenGridViewDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.view.descriptor.basic.BasicEvenGridViewDescriptor")]
    public class BasicEvenGridViewDescriptor extends BasicCompositeViewDescriptor implements IEvenGridViewDescriptor {

        private var _childViewDescriptors:ListCollectionView;
        private var _drivingDimension:String;
        private var _drivingDimensionCellCount:int;

        public function set childViewDescriptors(value:ListCollectionView):void {
            _childViewDescriptors = value;
        }
        override public function get childViewDescriptors():ListCollectionView {
            return _childViewDescriptors;
        }

        public function set drivingDimension(value:String):void {
            _drivingDimension = value;
        }
        public function get drivingDimension():String {
            return _drivingDimension;
        }

        public function set drivingDimensionCellCount(value:int):void {
            _drivingDimensionCellCount = value;
        }
        public function get drivingDimensionCellCount():int {
            return _drivingDimensionCellCount;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _childViewDescriptors = input.readObject() as ListCollectionView;
            _drivingDimension = input.readObject() as String;
            _drivingDimensionCellCount = input.readObject() as int;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_childViewDescriptors);
            output.writeObject(_drivingDimension);
            output.writeObject(_drivingDimensionCellCount);
        }
    }
}