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
      if (qx.core.Environment.get("qx.debug")) {
        remoteController = new qx.io.remote.Rpc(
            "http://localhost:8080/${rootArtifactId}-webapp/.qxrpc",
            "${package}.startup.remote.RemoteApplicationStartup"
        );
        remoteController.setCrossDomain(true);
      } else {
        remoteController = new qx.io.remote.Rpc(
            qx.io.remote.Rpc.makeServerURL(),
            "${package}.startup.remote.RemoteApplicationStartup"
        );
      }
      remoteController.setTimeout(600000);
      
      this.startController(remoteController);
    }
  }
});
