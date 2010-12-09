/* ************************************************************************

   Copyright:

   License:

   Authors:

************************************************************************ */

/**
 * This is the main application class
 */
qx.Class.define("${package}.startup.qooxdoo.Application",
{
  extend : org.jspresso.framework.application.frontend.Application,



  /*
  *****************************************************************************
     MEMBERS
  *****************************************************************************
  */

  members :
  {
    start : function() {
      var remoteController;
      if (qx.core.Variant.isSet("qx.debug", "on")) {
        remoteController = new qx.io.remote.Rpc(
            "http://localhost:8080/${rootArtifactId}-webapp/.qxrpc",
            "${package}.startup.qooxdoo.QooxdooApplicationStartup"
        );
        remoteController.setCrossDomain(true);
      } else {
        remoteController = new qx.io.remote.Rpc(
            qx.io.remote.Rpc.makeServerURL(),
            "${package}.startup.qooxdoo.QooxdooApplicationStartup"
        );
      }
      remoteController.setTimeout(600000);
      
      var qxController = new org.jspresso.framework.application.frontend.controller.qx.DefaultQxController(this, remoteController, "en");
      qxController.start();
    }
  }
});
