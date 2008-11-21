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

    import mx.collections.ListCollectionView;

    [RemoteClass(alias="org.jspresso.framework.gui.remote.RComboBox")]
    public class RComboBox extends RComponent {

        private var _icons:ListCollectionView;
        private var _translations:ListCollectionView;
        private var _values:ListCollectionView;

        public function set icons(value:ListCollectionView):void {
            _icons = value;
        }
        public function get icons():ListCollectionView {
            return _icons;
        }

        public function set translations(value:ListCollectionView):void {
            _translations = value;
        }
        public function get translations():ListCollectionView {
            return _translations;
        }

        public function set values(value:ListCollectionView):void {
            _values = value;
        }
        public function get values():ListCollectionView {
            return _values;
        }
    }
}