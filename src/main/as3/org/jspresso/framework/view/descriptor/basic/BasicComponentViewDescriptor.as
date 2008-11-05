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
    import org.granite.util.Enum;
    import org.jspresso.framework.view.descriptor.ELabelPosition;
    import org.jspresso.framework.view.descriptor.IComponentViewDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.view.descriptor.basic.BasicComponentViewDescriptor")]
    public class BasicComponentViewDescriptor extends BasicViewDescriptor implements IComponentViewDescriptor {

        private var _columnCount:int;
        private var _labelsPosition:ELabelPosition;
        private var _propertyViewDescriptors:ListCollectionView;
        private var _propertyWidths:Object;
        private var _renderedChildProperties:Object;

        public function set columnCount(value:int):void {
            _columnCount = value;
        }
        public function get columnCount():int {
            return _columnCount;
        }

        public function set labelsPosition(value:ELabelPosition):void {
            _labelsPosition = value;
        }
        public function get labelsPosition():ELabelPosition {
            return _labelsPosition;
        }

        public function set propertyViewDescriptors(value:ListCollectionView):void {
            _propertyViewDescriptors = value;
        }
        public function get propertyViewDescriptors():ListCollectionView {
            return _propertyViewDescriptors;
        }

        public function set propertyWidths(value:Object):void {
            _propertyWidths = value;
        }

        public function set renderedChildProperties(value:Object):void {
            _renderedChildProperties = value;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _columnCount = input.readObject() as int;
            _labelsPosition = Enum.readEnum(input) as ELabelPosition;
            _propertyViewDescriptors = input.readObject() as ListCollectionView;
            _propertyWidths = input.readObject() as Object;
            _renderedChildProperties = input.readObject() as Object;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_columnCount);
            output.writeObject(_labelsPosition);
            output.writeObject(_propertyViewDescriptors);
            output.writeObject(_propertyWidths);
            output.writeObject(_renderedChildProperties);
        }
    }
}