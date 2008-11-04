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
    import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
    import org.jspresso.framework.model.descriptor.IComponentDescriptor;
    import org.jspresso.framework.util.descriptor.DefaultDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.model.descriptor.basic.BasicCollectionDescriptor")]
    public class BasicCollectionDescriptor extends DefaultDescriptor implements ICollectionDescriptor {

        private var _collectionInterface:Class;
        private var _elementDescriptor:IComponentDescriptor;

        public function set collectionInterface(value:Class):void {
            _collectionInterface = value;
        }
        public function get collectionInterface():Class {
            return _collectionInterface;
        }

        public function set elementDescriptor(value:IComponentDescriptor):void {
            _elementDescriptor = value;
        }
        public function get elementDescriptor():IComponentDescriptor {
            return _elementDescriptor;
        }

        public function get modelType():Class {
            return null;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _collectionInterface = input.readObject() as Class;
            _elementDescriptor = input.readObject() as IComponentDescriptor;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_collectionInterface);
            output.writeObject(_elementDescriptor);
        }
    }
}