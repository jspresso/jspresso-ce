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
    import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
    import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.model.descriptor.basic.BasicCollectionPropertyDescriptor")]
    public class BasicCollectionPropertyDescriptor extends BasicRelationshipEndPropertyDescriptor implements ICollectionPropertyDescriptor {

        private var _manyToMany:Boolean;
        private var _orderingProperties:ListCollectionView;
        private var _referencedDescriptor:ICollectionDescriptor;

        public function set manyToMany(value:Boolean):void {
            _manyToMany = value;
        }
        public function get manyToMany():Boolean {
            return _manyToMany;
        }

        public function set orderingProperties(value:ListCollectionView):void {
            _orderingProperties = value;
        }
        public function get orderingProperties():ListCollectionView {
            return _orderingProperties;
        }

        public function set referencedDescriptor(value:ICollectionDescriptor):void {
            _referencedDescriptor = value;
        }
        public function get referencedDescriptor():ICollectionDescriptor {
            return _referencedDescriptor;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _manyToMany = input.readObject() as Boolean;
            _orderingProperties = input.readObject() as ListCollectionView;
            _referencedDescriptor = input.readObject() as ICollectionDescriptor;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_manyToMany);
            output.writeObject(_orderingProperties);
            output.writeObject(_referencedDescriptor);
        }
    }
}