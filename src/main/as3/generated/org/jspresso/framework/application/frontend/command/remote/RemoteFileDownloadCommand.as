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

		
    [RemoteClass(alias="org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand")]
    public class RemoteFileDownloadCommand extends RemoteCommand {

        private var _defaultFileName:String;
        private var _downloadUrl:String;
        private var _fileFilter:Object;
        private var _resourceId:String;

        public function set defaultFileName(value:String):void {
            _defaultFileName = value;
        }
        public function get defaultFileName():String {
            return _defaultFileName;
        }

        public function set downloadUrl(value:String):void {
            _downloadUrl = value;
        }
        public function get downloadUrl():String {
            return _downloadUrl;
        }

        public function set fileFilter(value:Object):void {
            _fileFilter = value;
        }
        public function get fileFilter():Object {
            return _fileFilter;
        }

        public function set resourceId(value:String):void {
            _resourceId = value;
        }
        public function get resourceId():String {
            return _resourceId;
        }
    }
}