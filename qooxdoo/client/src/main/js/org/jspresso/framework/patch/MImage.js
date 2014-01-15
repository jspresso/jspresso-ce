/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
qx.Mixin.define("org.jspresso.framework.patch.MImage", {
  members: {

    // Fixes bug # 8004
    _applySource: function (value, old) {
      var source = value;
      if (source && source.indexOf('data:') != 0) {
        var resourceManager = qx.util.ResourceManager.getInstance();

        if (resourceManager.has(source)) {
          this._setStyle("width", resourceManager.getImageWidth(source) / 16 + "rem");
          this._setStyle("height", resourceManager.getImageHeight(source) / 16 + "rem");
        }

        var foundHighResolutionSource = this._findHighResolutionSource(source);

        source = resourceManager.toUri(source);
        var ImageLoader = qx.io.ImageLoader;
        if (!ImageLoader.isFailed(source) && !ImageLoader.isLoaded(source)) {
          ImageLoader.load(source, this.__loaderCallback, this);
        }

        // If a no high-resolution version of the source was found, apply the source.
        if (foundHighResolutionSource == false) {
          this._setSource(source);
        }
      } else {
        this._setSource(source);
      }
    }
  }
});
