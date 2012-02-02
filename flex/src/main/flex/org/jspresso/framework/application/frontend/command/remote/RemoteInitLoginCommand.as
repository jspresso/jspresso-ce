/**
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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

    import org.jspresso.framework.gui.remote.RComponent;
    import org.jspresso.framework.gui.remote.RIcon;
		
    [RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand")]
    public class RemoteInitLoginCommand extends RemoteCommand {

        private var _loginView:RComponent;
        private var _message:String;
        private var _okIcon:RIcon;
        private var _okLabel:String;
        private var _title:String;

        public function RemoteInitLoginCommand() {
          //default constructor.
        }

        public function set loginView(value:RComponent):void {
            _loginView = value;
        }
        public function get loginView():RComponent {
            return _loginView;
        }

        public function set message(value:String):void {
            _message = value;
        }
        public function get message():String {
            return _message;
        }

        public function set okIcon(value:RIcon):void {
            _okIcon = value;
        }
        public function get okIcon():RIcon {
            return _okIcon;
        }

        public function set okLabel(value:String):void {
            _okLabel = value;
        }
        public function get okLabel():String {
            return _okLabel;
        }

        public function set title(value:String):void {
            _title = value;
        }
        public function get title():String {
            return _title;
        }
    }
}