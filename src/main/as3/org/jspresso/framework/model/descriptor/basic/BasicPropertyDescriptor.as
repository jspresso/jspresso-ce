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
    import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
    import org.jspresso.framework.util.descriptor.DefaultDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.model.descriptor.basic.BasicPropertyDescriptor")]
    public class BasicPropertyDescriptor extends DefaultDescriptor implements IPropertyDescriptor {

        private var _delegateClass:Class;
        private var _delegateClassName:String;
        private var _grantedRoles:ListCollectionView;
        private var _integrityProcessors:ListCollectionView;
        private var _mandatory:Boolean;
        private var _parentDescriptor:IPropertyDescriptor;
        private var _readOnly:Boolean;
        private var _readabilityGates:ListCollectionView;
        private var _unicityScope:String;
        private var _writabilityGates:ListCollectionView;

        public function get delegateClass():Class {
            return _delegateClass;
        }

        public function set delegateClassName(value:String):void {
            _delegateClassName = value;
        }
        public function get delegateClassName():String {
            return _delegateClassName;
        }

        public function set grantedRoles(value:ListCollectionView):void {
            _grantedRoles = value;
        }
        public function get grantedRoles():ListCollectionView {
            return _grantedRoles;
        }

        public function set integrityProcessors(value:ListCollectionView):void {
            _integrityProcessors = value;
        }
        public function get integrityProcessors():ListCollectionView {
            return _integrityProcessors;
        }

        public function set mandatory(value:Boolean):void {
            _mandatory = value;
        }
        public function get mandatory():Boolean {
            return _mandatory;
        }

        public function set parentDescriptor(value:IPropertyDescriptor):void {
            _parentDescriptor = value;
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

        public function set unicityScope(value:String):void {
            _unicityScope = value;
        }
        public function get unicityScope():String {
            return _unicityScope;
        }

        public function set writabilityGates(value:ListCollectionView):void {
            _writabilityGates = value;
        }
        public function get writabilityGates():ListCollectionView {
            return _writabilityGates;
        }

        public function get modelType():Class {
            return null;
        }

        public function get modifiable():Boolean {
            return false;
        }

        public function get overload():Boolean {
            return false;
        }

        public function get queryable():Boolean {
            return false;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _delegateClass = input.readObject() as Class;
            _delegateClassName = input.readObject() as String;
            _grantedRoles = input.readObject() as ListCollectionView;
            _integrityProcessors = input.readObject() as ListCollectionView;
            _mandatory = input.readObject() as Boolean;
            _parentDescriptor = input.readObject() as IPropertyDescriptor;
            _readOnly = input.readObject() as Boolean;
            _readabilityGates = input.readObject() as ListCollectionView;
            _unicityScope = input.readObject() as String;
            _writabilityGates = input.readObject() as ListCollectionView;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_delegateClass);
            output.writeObject(_delegateClassName);
            output.writeObject(_grantedRoles);
            output.writeObject(_integrityProcessors);
            output.writeObject(_mandatory);
            output.writeObject(_parentDescriptor);
            output.writeObject(_readOnly);
            output.writeObject(_readabilityGates);
            output.writeObject(_unicityScope);
            output.writeObject(_writabilityGates);
        }
    }
}