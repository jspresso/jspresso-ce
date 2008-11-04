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

package org.jspresso.framework.model.descriptor {

    import mx.collections.ListCollectionView;
    import org.jspresso.framework.util.descriptor.IIconDescriptor;

    public interface IComponentDescriptor extends IModelDescriptor, IIconDescriptor {

        function get componentContract():Class;

        function get computed():Boolean;

        function get declaredPropertyDescriptors():ListCollectionView;

        function get entity():Boolean;

        function get lifecycleInterceptors():ListCollectionView;

        function get orderingProperties():ListCollectionView;

        function get propertyDescriptors():ListCollectionView;

        function get purelyAbstract():Boolean;

        function get queryComponentContract():Class;

        function get queryableProperties():ListCollectionView;

        function get renderedProperties():ListCollectionView;

        function get serviceContracts():ListCollectionView;

        function get toStringProperty():String;

        function get unclonedProperties():ListCollectionView;
    }
}