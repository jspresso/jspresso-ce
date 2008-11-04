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
    import org.jspresso.framework.model.descriptor.IModelDescriptor;
    import org.jspresso.framework.util.descriptor.DefaultIconDescriptor;
    import org.jspresso.framework.view.descriptor.ISubViewDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.view.descriptor.basic.BasicSubviewDescriptor")]
    public class BasicSubviewDescriptor extends DefaultIconDescriptor implements ISubViewDescriptor {

        private var _grantedRoles:ListCollectionView;
        private var _modelDescriptor:IModelDescriptor;
        private var _readOnly:Boolean;
        private var _readabilityGates:ListCollectionView;
        private var _writabilityGates:ListCollectionView;

        public function set grantedRoles(value:ListCollectionView):void {
            _grantedRoles = value;
        }
        public function get grantedRoles():ListCollectionView {
            return _grantedRoles;
        }

        public function set modelDescriptor(value:IModelDescriptor):void {
            _modelDescriptor = value;
        }
        public function get modelDescriptor():IModelDescriptor {
            return _modelDescriptor;
        }

        public function set readOnly(value:Boolean):void {
            _readOnly = value;
        }
        public function get readOnly():Boolean {
            return _readOnly;
        }

        public function set readabilityGates(value:ListCollectionView):void {
            _readabilityGates = value;
        }
        public function get readabilityGates():ListCollectionView {
            return _readabilityGates;
        }

        public function set writabilityGates(value:ListCollectionView):void {
            _writabilityGates = value;
        }
        public function get writabilityGates():ListCollectionView {
            return _writabilityGates;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _grantedRoles = input.readObject() as ListCollectionView;
            _modelDescriptor = input.readObject() as IModelDescriptor;
            _readOnly = input.readObject() as Boolean;
            _readabilityGates = input.readObject() as ListCollectionView;
            _writabilityGates = input.readObject() as ListCollectionView;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_grantedRoles);
            output.writeObject(_modelDescriptor);
            output.writeObject(_readOnly);
            output.writeObject(_readabilityGates);
            output.writeObject(_writabilityGates);
        }
    }
}