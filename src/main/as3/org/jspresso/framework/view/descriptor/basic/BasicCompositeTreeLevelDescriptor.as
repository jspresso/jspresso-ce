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
    import org.granite.collections.IMap;
    import org.jspresso.framework.view.descriptor.ICompositeTreeLevelDescriptor;

    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.view.descriptor.basic.BasicCompositeTreeLevelDescriptor")]
    public class BasicCompositeTreeLevelDescriptor extends BasicTreeLevelDescriptor implements ICompositeTreeLevelDescriptor {

        private var _childrenDescriptorsMap:IMap;

        public function get childrenDescriptors():ListCollectionView {
            return null;
        }

        override public function readExternal(input:IDataInput):void {
            super.readExternal(input);
            _childrenDescriptorsMap = input.readObject() as IMap;
        }

        override public function writeExternal(output:IDataOutput):void {
            super.writeExternal(output);
            output.writeObject(_childrenDescriptorsMap);
        }
    }
}