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
 *
 * @asset (org/jspresso/framework/mobile/nav-mobile-menu-icon.png)
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

    this.__manager = new qx.ui.mobile.page.Manager(/*false*/);
    if (this.__manager.getMasterButton()) {
      this.__manager.getMasterButton().setIcon("org/jspresso/framework/mobile/nav-mobile-menu-icon.png");
      this.__manager.getMasterButton().setShow("icon");
      this.__manager.setHideMasterButtonCaption(this.translate("Hide"));
    }
  },

  members: {
    /** @type {qx.application.Mobile} */
    __application: null,
    /** @type {qx.ui.mobile.dialog.Popup} */
    __busyPopup: null,
    /** @type {qx.ui.mobile.page.Manager} */
    __manager: null,
    /** @type {qx.ui.mobile.page.NavigationPage} */
    __workspacesMasterPage: null,
    /** @type {org.jspresso.framework.gui.remote.RAction} */
    __exitAction: null,
    /** @type {Object} */
    __workspacePages: {},
    /** @type {String} */
    __displayedWorkspaceName: null,
    /** @type {qx.ui.mobile.page.NavigationPage} */
    __pageToRestore: null,


    _createViewFactory: function () {
      return new org.jspresso.framework.view.qx.mobile.MobileQxViewFactory(this, this, this);
    },

    /**
     * @param command {org.jspresso.framework.application.frontend.command.remote.RemoteCommand}
     * @return {undefined}
     */
    _handleCommand: function (command) {
      var c;
      if (command instanceof org.jspresso.framework.application.frontend.command.remote.mobile.RemoteBackCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.mobile.RemoteBackCommand} */
            command;
        this._handleBackCommand(c);
      } else if (command instanceof org.jspresso.framework.application.frontend.command.remote.RemoteEditCommand
          && !command.getTargetPeerGuid()) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteEditCommand} */
            command;
        this._handleEditCommand(null, c);
      } else {
        this.base(arguments, command)
      }
    },

    _showBusy: function (busy) {
      if (busy) {
        this.__busyPopup.show();
      } else {
        this.__busyPopup.hide();
      }
    },

    /**
     * @return {undefined}
     */
    _restart: function () {
      this.__workspacePages = {};
      this.__displayedWorkspaceName = null;
      if(this.__workspacesMasterPage) {
        this.__workspacesMasterPage.exclude();
      }
      this.base(arguments);
    },

    /**
     * @return {qx.ui.mobile.page.NavigationPage}
     */
    getCurrentPage: function () {
      var currentPage;
      var details = this.__manager.getDetailNavigation().getContent().getChildren();
      if (details) {
        for (var i = details.length - 1; i >= 0; i--) {
          if (details[i].getVisibility() == "visible") {
            currentPage = details[i];
            break;
          }
        }
      }
      return currentPage;
    },

    /**
     *
     * @param title {String}
     * @param message {String}
     * @param remoteDialogView {org.jspresso.framework.gui.remote.RComponent}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     * @param buttons {qx.ui.mobile.form.Button[]}
     * @param useCurrent {Boolean}
     * @param dimension {org.jspresso.framework.util.gui.Dimension}
     * @return {undefined}
     */
    _popupDialog: function (title, message, remoteDialogView, icon, buttons, useCurrent, dimension) {
      useCurrent = (typeof useCurrent == 'undefined') ? false : useCurrent;

      var dialogView = remoteDialogView;
      if (remoteDialogView instanceof org.jspresso.framework.gui.remote.RComponent) {
        dialogView = this._getViewFactory().createComponent(remoteDialogView);
      }

      var dialogContent = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
      var dialogMessage = new qx.ui.mobile.embed.Html(message);
      dialogMessage.addCssClass("form-row-group-title");

      dialogContent.add(dialogMessage);
      dialogContent.add(dialogView);
      for (var i = 0; i < buttons.length; i++) {
        dialogContent.add(buttons[i]);
      }

      var dialogPage = null;
      if (useCurrent && this._dialogStack.length > 1) {
        /** @type {qx.ui.mobile.page.NavigationPage} */
        var topDialogPage = this._dialogStack.pop()[0];
        if (topDialogPage) {
          dialogPage = topDialogPage;
        }
      }
      if(dialogPage == null) {
        dialogPage = new qx.ui.mobile.page.NavigationPage();
        dialogPage.addListener("initialize", function (e) {
          dialogPage.getContent().add(dialogContent);
        }, this);
        if (this._dialogStack.length == 1) {
          this.__pageToRestore = this.getCurrentPage();
        }
        //this._getViewFactory().setIcon(dialogPage, icon);
        this._dialogStack.push([dialogPage, null, null]);
        this.__manager.addDetail(dialogPage);
      } else {
        dialogPage.getContent().removeAll();
        dialogPage.getContent().add(dialogContent);
      }
      dialogPage.setTitle(title);
      if(remoteDialogView instanceof org.jspresso.framework.gui.remote.mobile.RMobilePageAware) {
        this._getViewFactory().installPageActions(remoteDialogView, dialogPage);
      }
      dialogPage.show({animation: "slideup"});
      if (this.__manager.getMasterButton()) {
        this.__manager.getMasterButton().exclude();
      }
    },

    /**
     * Close top most dialog.
     */
    _closeDialog: function () {
      if (this._dialogStack && this._dialogStack.length > 1) {
        /** @type {qx.ui.mobile.page.NavigationPage} */
        var topDialogPage = this._dialogStack.pop()[0];
        if (topDialogPage) {
          topDialogPage.exclude({animation: "slideup", reverse: true});
          //topDialogPage.destroy();
        }
      }
      if(this._dialogStack) {
        if (this._dialogStack.length == 1) {
          if (this.__pageToRestore) {
            this.__pageToRestore.show();
          }
          if (this.__manager.getMasterButton()) {
            this.__manager.getMasterButton().setVisibility("visible");
          }
        } else {
          this._dialogStack[this._dialogStack.length-1][0].show({animation: "slideup"});
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
      this.__workspacesMasterPage = new qx.ui.mobile.page.NavigationPage();
      this.__exitAction= exitAction;
      var workspacesNavigatorModel = new qx.data.Array();
      for (var i = 0; i < workspaceActions.getActions().length; i++) {
        workspacesNavigatorModel.push({
          workspaceName:   workspaceNames[i],
          workspaceAction: workspaceActions.getActions()[i]
        });
      }
      var workspaceNavigator = new qx.ui.mobile.list.List({
        configureItem: function (item, data, row) {
          item.setTitle(data.workspaceAction.getName());
          item.setSubtitle(data.workspaceAction.getDescription());
          if(data.workspaceAction.getIcon()) {
            item.setImage(data.workspaceAction.getIcon().getImageUrlSpec());
          }
          item.setShowArrow(true);
        }
      });
      workspaceNavigator.setModel(workspacesNavigatorModel);
      workspaceNavigator.addListener("changeSelection", function(evt) {
        var selectedIndex = evt.getData();
        var workspaceAction = workspacesNavigatorModel.getItem(selectedIndex).workspaceAction;
        var workspacePage = this.__workspacePages[workspaceNames[selectedIndex]];
        if(workspacePage) {
          workspacePage.show();
        }
        this.execute(workspaceAction);
      }, this);
      this.__workspacesMasterPage.addListener("initialize", function (e) {
        this.__workspacesMasterPage.getContent().add(workspaceNavigator);
      }, this);
      this.__workspacesMasterPage.setTitle(this.translate("Workspaces"));
      this.__manager.addMaster(this.__workspacesMasterPage);
      if (!this.__workspacesMasterPage.isTablet()) {
        this._getViewFactory().installPageMainAction(this.__workspacesMasterPage, exitAction);
      }
      this.__workspacesMasterPage.show();
    },

    /**
     * @param workspaceName {String}
     * @param workspaceView {org.jspresso.framework.gui.remote.RComponent}
     * @return {undefined}
     */
    _displayWorkspace: function (workspaceName, workspaceView) {
      if(workspaceName == this.__displayedWorkspaceName) {
        return;
      }
      this.__displayedWorkspaceName = workspaceName;
      if (workspaceView) {
        /** @type {qx.ui.mobile.page.NavigationPage} */
        var workspacePage = this.createComponent(workspaceView);
        this.__workspacePages[workspaceName] = workspacePage;
        if (workspacePage.isTablet()) {
          this._getViewFactory().installPageMainAction(workspacePage, this.__exitAction);
        } else {
          this._getViewFactory().linkNextPageBackButton(workspacePage, this.__workspacesMasterPage,
              workspaceView.getBackAction(), "cube");
        }
      }
      this.__workspacePages[workspaceName].show();
    },

    /**
     * @param backCommand {org.jspresso.framework.application.frontend.command.remote.mobile.RemoteBackCommand}
     */
    _handleBackCommand: function (backCommand) {
      this.getCurrentPage().back();
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

      if (messageCommand.getMessageIcon()) {
        var icon = new qx.ui.mobile.basic.Atom();
        this._getViewFactory().setIcon(icon, messageCommand.getMessageIcon());
        icon.setShow("icon");
        messageDialogContent.add(icon);
      }
      var label = new qx.ui.mobile.basic.Label(messageCommand.getMessage());
      label.setWrap(true);
      messageDialogContent.add(label);
      messageDialogContent.add(new qx.ui.mobile.form.Row());

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
    },

    /**
     * @param navigationPage {qx.ui.mobile.page.NavigationPage}
     * @return {undefined}
     */
    addDetailPage: function (navigationPage) {
      this.__manager.addDetail(navigationPage);
    },

    /**
     * @param targetPeer {org.jspresso.framework.gui.remote.RComponent}
     * @param addCardCommand {org.jspresso.framework.application.frontend.command.remote.RemoteAddCardCommand}
     * @return {undefined}
     */
    _handleAddCardCommand: function (targetPeer, addCardCommand) {
      this._getViewFactory().addCard(targetPeer.retrievePeer(), addCardCommand.getCard(), addCardCommand.getCardName());
    },

    /**
     * @param targetPeer {org.jspresso.framework.gui.remote.RComponent}
     * @param focusCommand {org.jspresso.framework.application.frontend.command.remote.RemoteFocusCommand}
     * @return {undefined}
     */
    _handleFocusCommand: function (targetPeer, focusCommand) {
      /** qx.ui.mobile.core.Widget */
      var component = targetPeer.retrievePeer();
      if(component instanceof qx.ui.mobile.page.NavigationPage) {
        this._getViewFactory().focus(/** qx.ui.mobile.page.NavigationPage */ component);
      }
    },

    /**
     * @param targetPeer {org.jspresso.framework.gui.remote.RComponent}
     * @param editCommand {org.jspresso.framework.application.frontend.command.remote.RemoteEditCommand}
     * @return {undefined}
     */
    _handleEditCommand: function (targetPeer, editCommand) {
      /** qx.ui.mobile.core.Widget */
      var component;
      if (targetPeer == null) {
        component = this.getCurrentPage();
      } else {
        component = targetPeer.retrievePeer();
      }
      if(component instanceof qx.ui.mobile.page.NavigationPage) {
        this._getViewFactory().edit(/** qx.ui.mobile.page.NavigationPage */ component);
      }
    },

    /**
     * @param abstractDialogCommand {org.jspresso.framework.application.frontend.command.remote.RemoteAbstractDialogCommand}
     * @return {undefined}
     */
    _handleDialogCommand: function (abstractDialogCommand) {
      var dialogButtons = [];
      for (var i = 0; i < abstractDialogCommand.getActions().length; i++) {
        dialogButtons.push(this._getViewFactory().createAction(abstractDialogCommand.getActions()[i]));
      }
      var dialogView;
      var icon;
      if (abstractDialogCommand
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteDialogCommand) {
        var dialogCommand = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteDialogCommand} */
            abstractDialogCommand;
        dialogView = dialogCommand.getView();
        icon = dialogCommand.getView().getIcon();
      }
      this._popupDialog(abstractDialogCommand.getTitle(), null, dialogView, icon, dialogButtons,
          abstractDialogCommand.getUseCurrent(), abstractDialogCommand.getDimension());
    },

    /**
     * @return {Array}
     */
    _getKeysToTranslate: function () {
      /** @type {Array} */
      var keysToTranslate = this.base(arguments);
      keysToTranslate = keysToTranslate.concat([
        "m_01", "m_02", "m_03", "m_04", "m_05", "m_06", "m_07", "m_08", "m_09", "m_10", "m_11", "m_12"]);
      return keysToTranslate;
    }
  }
});
