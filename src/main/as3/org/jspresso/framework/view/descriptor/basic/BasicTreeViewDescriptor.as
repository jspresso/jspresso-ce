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
    import org.jspresso.framework.util.IIconImageURLProvider;
    import org.jspresso.framework.view.descriptor.ITreeLevelDescriptor;
    import org.jspresso.framework.view.descriptor.ITreeViewDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.view.descriptor.basic.BasicTreeViewDescriptor")]
    public class BasicTreeViewDescriptor extends BasicViewDescriptor implements ITreeViewDescriptor {

        private var _childDescriptor:ITreeLevelDescriptor;
        private var _iconImageURLProvider:IIconImageURLProvider;
        private var _maxDepth:int;
        private var _renderedProperty:String;
        private var _rootSubtreeDescriptor:ITreeLevelDescriptor;

        public function set childDescriptor(value:ITreeLevelDescriptor):void {
            _childDescriptor = value;
        }

        public function set iconImageURLProvider(value:IIconImageURLProvider):void {
            _iconImageURLProvider = value;
        }
        public function get iconImageURLProvider():IIconImageURLProvider {
            return _iconImageURLProvider;
        }

        public function set maxDepth(value:int):void {
            _maxDepth = value;
        }
        public function get maxDepth():int {
            return _maxDepth;
        }

        public function set renderedProperty(value:String):void {
            _renderedProperty = value;
        }

        public function set rootSubtreeDescriptor(value:ITreeLevelDescriptor):void {
            _rootSubtreeDescriptor = value;
        }
        public function get rootSubtreeDescriptor():ITreeLevelDescriptor {
            return _rootSubtreeDescriptor;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _childDescriptor = input.readObject() as ITreeLevelDescriptor;
            _iconImageURLProvider = input.readObject() as IIconImageURLProvider;
            _maxDepth = input.readObject() as int;
            _renderedProperty = input.readObject() as String;
            _rootSubtreeDescriptor = input.readObject() as ITreeLevelDescriptor;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_childDescriptor);
            output.writeObject(_iconImageURLProvider);
            output.writeObject(_maxDepth);
            output.writeObject(_renderedProperty);
            output.writeObject(_rootSubtreeDescriptor);
        }
    }
}