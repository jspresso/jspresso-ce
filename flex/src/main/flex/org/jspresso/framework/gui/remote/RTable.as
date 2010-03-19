/**
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

		
    [RemoteClass(alias="org.jspresso.framework.gui.remote.RTable")]
    public class RTable extends RCollectionComponent {

        private var _columns:Array;
        private var _columnIds:Array;
        private var _sortingAction:RAction;
        private var _horizontallyScrollable:Boolean;
        private var _sortable:Boolean;

        public function RTable() {
          //default constructor.
        }

        public function set columns(value:Array):void {
            _columns = value;
        }
        public function get columns():Array {
            return _columns;
        }

        public function set columnIds(value:Array):void {
            _columnIds = value;
        }
        public function get columnIds():Array {
            return _columnIds;
        }

        public function set sortingAction(value:RAction):void {
            _sortingAction = value;
        }
        public function get sortingAction():RAction {
            return _sortingAction;
        }

        public function set horizontallyScrollable(value:Boolean):void {
            _horizontallyScrollable = value;
        }
        public function get horizontallyScrollable():Boolean {
            return _horizontallyScrollable;
        }

        public function set sortable(value:Boolean):void {
            _sortable = value;
        }
        public function get sortable():Boolean {
            return _sortable;
        }
    }
}