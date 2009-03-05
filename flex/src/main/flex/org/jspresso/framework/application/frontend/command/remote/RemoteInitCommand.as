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


package org.jspresso.framework.application.frontend.command.remote {

		
    [RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand")]
    public class RemoteInitCommand extends RemoteCommand {

        private var _actions:Array;
        private var _helpActions:Array;
        private var _workspaceActions:Array;

        public function set actions(value:Array):void {
            _actions = value;
        }
        public function get actions():Array {
            return _actions;
        }

        public function set helpActions(value:Array):void {
            _helpActions = value;
        }
        public function get helpActions():Array {
            return _helpActions;
        }

        public function set workspaceActions(value:Array):void {
            _workspaceActions = value;
        }
        public function get workspaceActions():Array {
            return _workspaceActions;
        }
    }
}