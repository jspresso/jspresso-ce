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
qx.Class.define("org.jspresso.framework.application.frontend.controller.qx.mobile.MobileQxController", {

  extend: org.jspresso.framework.application.frontend.controller.qx.AbstractQxController,

  implement: [
  ],

  statics: {
  },

  /**
   * @param application {qx.application.Mobile}
   * @param remoteController {qx.io.remote.Rpc}
   * @param userLanguage {String}
   */
  construct: function (application, remoteController, userLanguage) {
    this.base(arguments, remoteController, userLanguage);
    this._application = application;

    var busyIndicator = new qx.ui.mobile.dialog.BusyIndicator(this.translate("Wait") + "...");
    this.__busyPopup = new qx.ui.mobile.dialog.Popup(busyIndicator);
    this.__busyPopup.setTitle(this.translate("Loading") + "...");

    this._manager = new qx.ui.mobile.page.Manager(false);
    if (this._manager.getMasterButton()) {
      this._manager.getMasterButton().setVisibility("excluded");
      this._manager.setHideMasterButtonCaption(this.translate("Hide"));
    }
  },

  members: {
    /** @type {qx.application.Mobile} */
    _application: null,
    /** @type {qx.ui.mobile.dialog.Popup} */
    __busyPopup: null,
    /** @type {qx.ui.mobile.page.Manager} */
    _manager: null,
    /** @type {qx.ui.mobile.container.Composite} */
    __workspacesNavigator: null,
    /** @type {qx.ui.mobile.page.NavigationPage} */
    _workspacesNavigationPage: null,

    __workspacesPages: {},


    _createViewFactory: function () {
      return new org.jspresso.framework.view.qx.mobile.MobileQxViewFactory(this, this, this);
    },

    _showBusy: function (busy) {
      if (busy) {
        this.__busyPopup.show();
      } else {
        this.__busyPopup.hide();
      }
    },

    /**
     *
     * @param title {String}
     * @param message {String}
     * @param dialogView {qx.ui.mobile.core.Widget}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     * @param buttons {qx.ui.mobile.form.Button[]}
     * @param useCurrent {Boolean}
     * @param dimension {org.jspresso.framework.util.gui.Dimension}
     * @return {undefined}
     */
    _popupDialog: function (title, message, dialogView, icon, buttons, useCurrent, dimension) {
      useCurrent = (typeof useCurrent == 'undefined') ? false : useCurrent;

      var dialogContent = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
      var dialogMessage = new qx.ui.mobile.embed.Html(message);

      dialogContent.add(dialogMessage);
      dialogContent.add(dialogView);
      for (var i = 0; i < buttons.length; i++) {
        dialogContent.add(buttons[i]);
      }

      if (useCurrent && this._dialogStack && this._dialogStack.length > 1) {
        /** @type {qx.ui.mobile.dialog.Popup} */
        var topDialog = this._dialogStack.pop()[0];
        if (topDialog) {
          topDialog.hide();
          topDialog.destroy();
        }
      }

      var dialogPopup = new qx.ui.mobile.dialog.Popup(dialogContent);
      dialogPopup.setTitle(title);
      this._getViewFactory().setIcon(dialogPopup, icon);
      dialogPopup.setModal(true);
      dialogPopup.setHideOnBlockerClick(false);
      this._dialogStack.push([dialogPopup, null, null]);
      dialogPopup.show();
      this._getViewFactory().focus(dialogContent);
    },

    /**
     * Close top most dialog.
     */
    _closeDialog: function () {
      if (this._dialogStack && this._dialogStack.length > 1) {
        /** @type {qx.ui.mobile.dialog.Popup} */
        var topDialog = this._dialogStack.pop()[0];
        if (topDialog) {
          topDialog.hide();
          topDialog.destroy();
        }
      }
    },

    /**
     * @param workspaceNames {String[]}
     * @param workspaceActions {org.jspresso.framework.gui.remote.RActionList}
     * @param exitAction {org.jspresso.framework.gui.remote.RAction}
     * @param navigationActions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param actions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param secondaryActions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param helpActions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param size {org.jspresso.framework.util.gui.Dimension}
     * @return {undefined}
     *
     */
    _initApplicationFrame: function (workspaceNames, workspaceActions, exitAction, navigationActions, actions,
                                     secondaryActions, helpActions, size) {
      this.__workspacesNavigator = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
      for (var i = 0; i < workspaceActions.getActions().length; i++) {
        var workspaceAction = workspaceActions.getActions()[i];
        var workspaceSection = new qx.ui.mobile.container.Collapsible("<html><img src=\""
            + workspaceAction.getIcon().getImageUrlSpec() + "\"/> " + workspaceAction.getName() + "</html>");
        workspaceSection.setUserData("model", {
          name: workspaceNames[i],
          action: workspaceAction
        });
        if (i == 0) {
          workspaceSection.setCollapsed(false);
        } else {
          workspaceSection.setCollapsed(true);
        }
        workspaceSection.addListener("changeCollapsed", function (evt) {
          var openedWsView = /**@type {qx.ui.mobile.Collapsible}*/ evt.getTarget();
          if (!openedWsView.getCollapsed()) {
            for (var j = 0; j < this.__workspacesNavigator.getChildren().length; j++) {
              if (this.__workspacesNavigator.getChildren()[j] != openedWsView) {
                (/**@type {qx.ui.mobile.container.Collapsible}*/ this.__workspacesNavigator.getChildren()[j]).setCollapsed(true);
              }
            }
            var workspaceAction = openedWsView.getUserData("model")["action"]
            this.execute(workspaceAction);
          }
        }, this);
        this.__workspacesNavigator.add(workspaceSection);
      }
      this._workspacesNavigationPage = new qx.ui.mobile.page.NavigationPage();
      this._workspacesNavigationPage.setTitle(this.translate("Workspaces"));
      this._workspacesNavigationPage.addListener("initialize", function (e) {
        var content = this._workspacesNavigationPage.getContent();
        content.add(this.__workspacesNavigator, {flex: 1});
      }, this);
      this._manager.addMaster(this._workspacesNavigationPage);
      if (this._manager.getMasterButton()) {
        this._manager.getMasterButton().setVisibility("visible");
        this._manager._onMasterButtonTap();
      }
      this._workspacesNavigationPage.show();
    },

    /**
     *
     * @param workspaceName {String}
     * @param workspaceView {org.jspresso.framework.gui.remote.RComponent}
     * @return {undefined}
     */
    _displayWorkspace: function (workspaceName, workspaceView) {
      if (workspaceView) {
        var workspaceNavigator = null;
        if (workspaceView instanceof org.jspresso.framework.gui.remote.RSplitContainer) {
          var wv = /** @type {org.jspresso.framework.gui.remote.RSplitContainer } */ workspaceView;
          workspaceNavigator = wv.getLeftTop();
          workspaceView = wv.getRightBottom();
        }
        var workspaceViewUI = this.createComponent(workspaceView) /*new qx.ui.mobile.form.Label(workspaceName)*/;
        var workspacePage = new qx.ui.mobile.page.NavigationPage();
        workspacePage.addListener("initialize", function (e) {
          var content = workspacePage.getContent();
          content.add(workspaceViewUI, {flex: 1});
        }, this);
        this._manager.addDetail(workspacePage);
        if (!workspacePage.isTablet()) {
          workspacePage.setShowBackButton(true);
          workspacePage.setBackButtonText(this.translate("Workspaces"));
          workspacePage.addListener("back", function () {
            this._workspacesNavigationPage.show({animation: "cube", reverse: true});
          }, this);
        }
        this.__workspacesPages[workspaceName] = workspacePage;
        if (workspaceNavigator) {
          var workspaceNavigatorUI = this.createComponent(workspaceNavigator);
          workspaceNavigatorUI.addListener("tap", function (e) {
            this.__workspacesPages[workspaceName].show();
          }, this);
          var existingWorkspaceSections = this.__workspacesNavigator.getChildren();
          var existingWorkspaceSection = null;
          for (var i = 0; existingWorkspaceSection == null && i < existingWorkspaceSections.length; i++) {
            var child = existingWorkspaceSections[i];
            if (child.getUserData("model").name == workspaceName) {
              existingWorkspaceSection = child;
            }
          }
          if (existingWorkspaceSection) {
            existingWorkspaceSection.add(workspaceNavigatorUI);
          }
        }
      }
      var children = this.__workspacesNavigator.getChildren();
      for (var i = 0; i < children.length; i++) {
        var child = children[i];
        if (child.getUserData("model").name == workspaceName) {
          child.setCollapsed(false);
        }
      }
    },

    /**
     * @param messageCommand {org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand}
     */
    _handleMessageCommand: function (messageCommand) {
      var messageDialogContent = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());

      var messageDialog = new qx.ui.mobile.dialog.Popup(messageDialogContent);
      messageDialog.setTitle(messageCommand.getTitle());
      messageDialog.setModal(true);
      messageDialog.setHideOnBlockerClick(false);

      this._getViewFactory().setIcon(messageDialog, messageCommand.getTitleIcon());

      var message = new qx.ui.mobile.basic.Atom(messageCommand.getMessage());
      this._getViewFactory().setIcon(message, messageCommand.getMessageIcon());
      messageDialogContent.add(message);

      var buttonBox = new qx.ui.mobile.container.Composite();
      buttonBox.setLayout(new qx.ui.mobile.layout.HBox("right"));
      messageDialogContent.add(buttonBox);

      var mc;
      if (messageCommand instanceof org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCommand) {
        mc = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCommand } */ messageCommand;
        var yesButton = this._getViewFactory().createYesButton();
        this._getViewFactory().addButtonListener(yesButton, function (event) {
          messageDialog.hide();
          messageDialog.destroy();
          this.execute(mc.getYesAction());
        }, this);
        buttonBox.add(yesButton);

        var noButton = this._getViewFactory().createNoButton();
        this._getViewFactory().addButtonListener(noButton, function (event) {
          messageDialog.hide();
          messageDialog.destroy();
          this.execute(mc.getNoAction());
        }, this);
        buttonBox.add(noButton);

        if (messageCommand
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCancelCommand) {
          mc = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCancelCommand } */ messageCommand;
          var cancelButton = this._getViewFactory().createCancelButton();
          this._getViewFactory().addButtonListener(cancelButton, function (event) {
            messageDialog.hide();
            messageDialog.destroy();
            this.execute(mc.getCancelAction());
          }, this);
          buttonBox.add(cancelButton);
        }
      } else if (messageCommand
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteOkCancelCommand) {
        mc = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteOkCancelCommand } */ messageCommand;
        var okButton = this._getViewFactory().createOkButton();
        this._getViewFactory().addButtonListener(okButton, function (event) {
          messageDialog.hide();
          messageDialog.destroy();
          this.execute(mc.getOkAction());
        }, this);
        buttonBox.add(okButton);

        var cancelButton = this._getViewFactory().createCancelButton();
        this._getViewFactory().addButtonListener(cancelButton, function (event) {
          messageDialog.hide();
          messageDialog.destroy();
          this.execute(mc.getCancelAction());
        }, this);
        buttonBox.add(cancelButton);
      } else {
        var okButton = this._getViewFactory().createOkButton();
        this._getViewFactory().addButtonListener(okButton, function (event) {
          this._application.getRoot().remove(messageDialog);
          messageDialog.hide();
          messageDialog.destroy();
        }, this);
        buttonBox.add(okButton);
      }

      messageDialog.show();
    },

    /**
     * @param clipboardCommand {org.jspresso.framework.application.frontend.command.remote.RemoteClipboardCommand}
     */
    _handleClipboardCommand: function (clipboardCommand) {
      throw new Error("RemoteClipboardCommand is not supported in mobile environmnents.");
    },

    /**
     * @param remoteUpdateStatusCommand {org.jspresso.framework.application.frontend.command.remote.RemoteUpdateStatusCommand}
     * @return {undefined}
     */
    _updateStatusCommand: function (remoteUpdateStatusCommand) {
      // TODO implement
    }



  }
});
