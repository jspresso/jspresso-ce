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


package org.jspresso.framework.gui.remote {


    [Bindable]
    [RemoteClass(alias="org.jspresso.framework.gui.remote.REnumComponent")]
    public class REnumComponent extends RComponent {

        private var _renderingIcons:Object;
        private var _renderingTranslations:Object;

        public function set renderingIcons(value:Object):void {
            _renderingIcons = value;
        }
        public function get renderingIcons():Object {
            return _renderingIcons;
        }

        public function set renderingTranslations(value:Object):void {
            _renderingTranslations = value;
        }
        public function get renderingTranslations():Object {
            return _renderingTranslations;
        }
    }
}