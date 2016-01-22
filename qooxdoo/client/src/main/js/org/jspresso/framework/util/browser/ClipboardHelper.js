/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
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

//noinspection FunctionWithInconsistentReturnsJS
/**
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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

qx.Class.define("org.jspresso.framework.util.browser.ClipboardHelper", {
  statics: {

    /**
     * tries to copy text to the clipboard of the underlying operating
     * system
     *
     * sources: http://www.krikkit.net/howto_javascript_copy_clipboard.html
     * http://www.xulplanet.com/tutorials/xultu/clipboard.html
     * http://www.codebase.nl/index.php/command/viewcode/id/174
     *
     * works only in Mozilla and Internet Explorer In Mozilla, add this line
     * to your prefs.js file in your Mozilla user profile directory
     * user_pref("signed.applets.codebase_principal_support", true); or
     * change the setting from within the browser with calling the
     * "about:config" page
     * @lint ignoreDeprecated(alert)
     */
    copyToSystemClipboard: function (dataTransfers) {
      if (window.clipboardData) {
        var i;
        var flavor;
        var text;
        for (i = 0; i < dataTransfers.length; i += 2) {
          flavor = dataTransfers[i];
          text = dataTransfers[i + 1];
          if (!flavor) {
            // default
            flavor = "text/unicode";
          }
          // IE
          if (flavor == "text/unicode") {
            window.clipboardData.setData("Text", text);
          }
        }
//      } else if (window.netscape) {
//        // Mozilla, Firefox etc.
//        try {
//          netscape.security.PrivilegeManager
//              .enablePrivilege("UniversalXPConnect");
//        } catch (e) {
//          alert(  "Because of tight security settings in Mozilla / Firefox you cannot copy "
//                  + "to the system clipboard at the moment. Please open the 'about:config' page "
//                  + "in your browser and change the preference 'signed.applets.codebase_principal_support' to 'true'.");
//          return false;
//        }
//        // we could successfully enable the privilege
//        var clip = Components.classes['@mozilla.org/widget/clipboard;1']
//            .createInstance(Components.interfaces.nsIClipboard);
//        if (!clip)
//          return;
//        var trans = Components.classes['@mozilla.org/widget/transferable;1']
//            .createInstance(Components.interfaces.nsITransferable);
//        if (!trans)
//          return;
//
//	      for (i = 0; i < dataTransfers.length; i+=2) {
//	        flavor = dataTransfers[i];
//	        text = dataTransfers[i+1];
//	        if (!flavor) {
//	          // default
//	          flavor = "text/unicode";
//	        }
//
//	        trans.addDataFlavor(flavor);
//	        var str = {};
//	        var len = {};
//	        var str = Components.classes["@mozilla.org/supports-string;1"]
//	            .createInstance(Components.interfaces.nsISupportsString);
//	        str.data = text;
//	        trans.setTransferData(flavor, str, text.length * 2);
//        }
//        var clipId = Components.interfaces.nsIClipboard;
//        if (!clip)
//          return false;
//        clip.setData(trans, null, clipId.kGlobalClipboard);
//        return true;
      } else {
        alert("Your browser does not support copying to the clipboard!");
      }
    }
  }
});
