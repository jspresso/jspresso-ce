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

package org.jspresso.framework.binding {

    public interface IValueConnector extends IConnector {

        function get connectorPath():String;

        function set connectorValue(value:Object):void;
        function get connectorValue():Object;

        function set locallyReadable(value:Boolean):void;

        function set locallyWritable(value:Boolean):void;

        function set modelConnector(value:IValueConnector):void;
        function get modelConnector():IValueConnector;

        function set parentConnector(value:ICompositeValueConnector):void;
        function get parentConnector():ICompositeValueConnector;

        function get readable():Boolean;

        function get writable():Boolean;
    }
}