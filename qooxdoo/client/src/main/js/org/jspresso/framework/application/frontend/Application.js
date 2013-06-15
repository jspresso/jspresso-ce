/*******************************************************************************
 *
 * Copyright:
 *
 * License:
 *
 * Authors:
 *
 ******************************************************************************/

/*******************************************************************************
 *
 * #use(org.jspresso.framework.gui.remote.RAction)
 * #use(org.jspresso.framework.gui.remote.RActionField)
 * #use(org.jspresso.framework.gui.remote.RActionList)
 * #use(org.jspresso.framework.gui.remote.RBorderContainer)
 * #use(org.jspresso.framework.gui.remote.RCardContainer)
 * #use(org.jspresso.framework.gui.remote.RCheckBox)
 * #use(org.jspresso.framework.gui.remote.RCollectionComponent)
 * #use(org.jspresso.framework.gui.remote.RColorField)
 * #use(org.jspresso.framework.gui.remote.RComboBox)
 * #use(org.jspresso.framework.gui.remote.RComponent)
 * #use(org.jspresso.framework.gui.remote.RConstrainedGridContainer)
 * #use(org.jspresso.framework.gui.remote.RContainer)
 * #use(org.jspresso.framework.gui.remote.RDateField)
 * #use(org.jspresso.framework.gui.remote.RDecimalComponent)
 * #use(org.jspresso.framework.gui.remote.RDecimalField)
 * #use(org.jspresso.framework.gui.remote.RDurationField)
 * #use(org.jspresso.framework.gui.remote.REvenGridContainer)
 * #use(org.jspresso.framework.gui.remote.RForm)
 * #use(org.jspresso.framework.gui.remote.RIcon)
 * #use(org.jspresso.framework.gui.remote.RImageComponent)
 * #use(org.jspresso.framework.gui.remote.RIntegerField)
 * #use(org.jspresso.framework.gui.remote.RLabel)
 * #use(org.jspresso.framework.gui.remote.RList)
 * #use(org.jspresso.framework.gui.remote.RNumericComponent)
 * #use(org.jspresso.framework.gui.remote.RPasswordField)
 * #use(org.jspresso.framework.gui.remote.RPercentField)
 * #use(org.jspresso.framework.gui.remote.RSecurityComponent)
 * #use(org.jspresso.framework.gui.remote.RSplitContainer)
 * #use(org.jspresso.framework.gui.remote.RTabContainer)
 * #use(org.jspresso.framework.gui.remote.RTable)
 * #use(org.jspresso.framework.gui.remote.RTextArea)
 * #use(org.jspresso.framework.gui.remote.RTextField)
 * #use(org.jspresso.framework.gui.remote.RTimeField)
 * #use(org.jspresso.framework.gui.remote.RTree)
 *
 * #use(org.jspresso.framework.util.gui.Dimension)
 * #use(org.jspresso.framework.util.gui.CellConstraints)
 * #use(org.jspresso.framework.util.gui.Font)
 *
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteActionCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteAddCardCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteCloseDialogCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteDialogCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteFileCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteLocaleCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteLoginCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteOkCancelCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteOpenUrlCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteReadabilityCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteRestartCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteSortCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteStartCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteWorkspaceDisplayCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteWritabilityCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCancelCommand)
 * #use(org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCommand)
 *
 ******************************************************************************/

qx.Class.define("org.jspresso.framework.application.frontend.Application", {
  extend: qx.application.Standalone,

  members: {
    /** @type org.jspresso.framework.application.frontend.controller.qx.DefaultQxController */
    __qxController: null,

    main: function () {
      this.base(arguments);

      qx.Class.patch(qx.ui.form.CheckBox, org.jspresso.framework.patch.MCheckBox);

      qx.Theme.include(qx.theme.modern.Appearance, collapsablepanel.theme.modern.Appearance);
      qx.Theme.include(qx.theme.classic.Appearance, collapsablepanel.theme.classic.Appearance);

      // In order to cope with dates the old "new Date(Date.UTC..." way
      // Not used anymore. Jspresso now handles dates using the ISO8601
      // spec
      // qx.io.remote.Rpc.CONVERT_DATES = true;

      // Enable logging in debug variant
      if (qx.core.Environment.get("qx.debug")) {
        {
          // support native logging capabilities, e.g. Firebug for Firefox
          //noinspection BadExpressionStatementJS
          qx.log.appender.Native;
          // support additional cross-browser console. Press F7 to toggle visibility
          //noinspection BadExpressionStatementJS
          qx.log.appender.Console;
        }
      }
      this.start();
    },

    startController: function (remoteController) {
      this.__qxController = new org.jspresso.framework.application.frontend.controller.qx.DefaultQxController(this,
          remoteController, "en");
      this.__qxController.start();
    },

    close: function () {
      this.base(arguments);
      this.__qxController.stop()
    }
  }
});
