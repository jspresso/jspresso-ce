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

/**
 * @asset (org/jspresso/framework/mobile/bookmark/android3_bookmark.png)
 * @asset (org/jspresso/framework/mobile/bookmark/android4_bookmark.png)
 * @asset (org/jspresso/framework/mobile/bookmark/blackberry.png)
 * @asset (org/jspresso/framework/mobile/bookmark/playbook_bookmark.png)
 * @asset (org/jspresso/framework/mobile/bookmark/safari_forward.png)
 * @asset (org/jspresso/framework/mobile/bookmark/safari_ios7_forward.png)
 */
qx.Class.define("org.jspresso.framework.application.frontend.controller.qx.mobile.MobileQxController", {

  extend: org.jspresso.framework.application.frontend.controller.qx.AbstractQxController,

  implement: [
  ],

  statics: {
  },

  /**
   * @param remoteController {qx.io.remote.Rpc}
   * @param userLanguage {String}
   */
  construct: function (remoteController, userLanguage) {
    this.base(arguments, remoteController, userLanguage);
    this.__applicationContainer = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.Card());
    this.__applicationContainer.addCssClass("jspresso-root");
    this._getApplication().getRoot().add(this.__applicationContainer, {flex: 1});
    var deviceType = qx.core.Environment.get("device.type");
    this.__isTablet = false;
    if (deviceType == "tablet") {
      this.__isTablet = true;
    } else if (deviceType == "desktop") {
      var boundingRect = this._getApplication().getRoot().getContainerElement().getBoundingClientRect();
      if (boundingRect.width > 500) {
        this.__isTablet = true;
      }
    }
    this.__busyIndicator = new qx.ui.mobile.dialog.BusyIndicator(this.translate("Wait") + "...");
    this.__busyPopup = new qx.ui.mobile.dialog.Popup(this.__busyIndicator);
    this.__busyPopup.setTitle(this.translate("Loading") + "...");
    this.__manager = this.__createManager();
    this.__routing = this.__createRouting();
  },

  members: {
    /** @type {qx.ui.mobile.dialog.BusyIndicator} */
    __busyIndicator: null,
    /** @type {qx.ui.mobile.dialog.Popup} */
    __busyPopup: null,
    /** @type {Boolean} */
    __busy: false,
    /** @type {qx.ui.mobile.page.NavigationPage} */
    __blankPage: null,
    /** @type {org.jspresso.framework.application.frontend.controller.qx.mobile.EnhancedManager} */
    __manager: null,
    /** @type {qx.ui.mobile.page.NavigationPage} */
    __workspacesMasterPage: null,
    /** @type {org.jspresso.framework.gui.remote.RAction} */
    __exitAction: null,
    /** @type {Object} */
    __workspacePages: {},
    /** @type {String} */
    __displayedWorkspaceName: null,
    /** @type {qx.application.Routing} */
    __routing: null,
    /** @type {boolean} */
    __isTablet: false,
    /** @type {boolean} */
    __restoreMasterOnClose: false,
    /** @type {Object} */
    __drawers: {},
    /** @type {qx.ui.mobile.container.Composite} */
    __applicationContainer: null,
    /** @type {qx.ui.mobile.container.Composite} */
    __managerContainer: null,
    /** @type {qx.ui.mobile.page.NavigationPage[]} */
    __animationQueue: null,
    /** @type {qx.ui.mobile.dialog.Popup[]} */
    __messageQueue: null,
    /** @type {qx.ui.mobile.page.NavigationPage} */
    __savedCurrentPage: null,


    showPage: function (page, animation, back) {
      if (this.getCurrentPage() != page) {
        this._getViewFactory().loseFocus();
        var detailCardLayout = this._getManager().getDetailNavigation().getLayout();
        if (page.getLayoutParent().getLayoutParent() == this._getManager().getDetailNavigation()
            && detailCardLayout.getShowAnimation()) {
          if (this.__animationQueue != null) {
            this.__animationQueue.push({page: page, animation: animation, back: back});
          } else {
            this.__animationQueue = [];
            page.show({animation: animation, reverse: back});
            detailCardLayout.addListenerOnce("animationEnd", function (e) {
              this.__dequeueAnimation();
            }, this);
          }
        } else {
          page.show({animation: animation, reverse: back});
        }
      }
    },

    __dequeueAnimation: function () {
      if (this.__animationQueue != null) {
        if (this.__animationQueue.length > 0) {
          var detailCardLayout = this._getManager().getDetailNavigation().getLayout();
          detailCardLayout.addListenerOnce("animationEnd", function (e) {
            this.__dequeueAnimation();
          }, this);
          var anim = this.__animationQueue.splice(0, 1)[0];
          anim.page.show({animation: anim.animation, reverse: anim.back});
        }
        if (this.__animationQueue.length == 0) {
          this.__animationQueue = null;
        }
      }
      if (!this.__animationQueue && this.__messageQueue) {
        for (var i = 0; i < this.__messageQueue.length; i++) {
          var md = this.__messageQueue[i];
          qx.event.Timer.once(function () {
            md.show();
          }, this, 100);
        }
        this.__messageQueue = null;
      }
    },

    __showDialog: function (dialog) {
      if (this.isAnimating()) {
        if (this.__messageQueue == null) {
          this.__messageQueue = [];
        }
        this.__messageQueue.push(dialog);
      } else {
        dialog.show();
      }
    },

    isAnimating: function () {
      return this.__animationQueue != null;
    },

    hasAnimationQueued: function () {
      return this.__animationQueue != null && this.__animationQueue.length > 0;
    },

    /**
     * @param action {org.jspresso.framework.gui.remote.RAction}
     * @param actionEvent {org.jspresso.framework.gui.remote.RActionEvent}
     * @param actionCallback {function}
     * @return {undefined}
     */
    execute: function (action, actionEvent, actionCallback) {
      this._getViewFactory().loseFocus();
      this.base(arguments, action, actionEvent, actionCallback);
    },

    /**
     * @return {undefined}
     */
    _performLogin: function () {
      this._getViewFactory().loseFocus();
      this.base(arguments);
    },

    /**
     * @return {org.jspresso.framework.application.frontend.controller.qx.mobile.EnhancedManager}
     */
    _getManager: function () {
      return this.__manager;
    },

    __routeToPage: function (page, data, animation) {
      var back = false;
      var currentPage = this.getCurrentPage();
      if (data && data.customData) {
        if (data.customData.animation) {
          animation = data.customData.animation;
        }
        if (data.customData.fromHistory) {
          if (data.customData.action == "back") {
            if (currentPage.getShowBackButton()) {
              currentPage.back();
            }
            return;
          }
        } else if (data.customData.back) {
          back = true;
        }
      }
      this.showPage(page, animation, back);
    },

    /**
     * @return {qx.application.Routing}
     */
    __createRouting: function () {
      var routing = new qx.application.Routing();
      routing.onGet("/*", function (data) {
        if (this.isTablet()) {
          var page = this.__workspacePages[this.__displayedWorkspaceName];
        } else {
          page = this.__workspacesMasterPage;
        }
        this.__routeToPage(page, data, "cube");
      }, this);
      routing.onGet("/workspaces", function (data) {
        if (this.__workspacesMasterPage) {
          this.__routeToPage(this.__workspacesMasterPage, data, "cube");
        }
      }, this);
      routing.onGet("/workspace/{workspaceName}", function (data) {
        var workspacePage = this.__workspacePages[data.params.workspaceName];
        if (workspacePage) {
          this.__routeToPage(workspacePage, data, "cube");
        }
//        if (this.isTablet()) {
//          this._getManager().getMasterContainer().hide();
//        }
      }, this);
      routing.onGet("/page/{pageGuid}", function (data) {
        /** @type {org.jspresso.framework.gui.remote.mobile.RMobilePage} */
        var remotePage = this.getRegistered(data.params.pageGuid);
        if (remotePage) {
          var page = remotePage.retrievePeer();
          if (page) {
            this.__routeToPage(page, data, "slide");
          }
        }
      }, this);
      return routing;
    },

    isTablet: function () {
      return this.__isTablet;
    },

    /**
     * @return {org.jspresso.framework.application.frontend.controller.qx.mobile.EnhancedManager}
     */
    __createManager: function () {
      this.__managerContainer = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
      this.__managerContainer.addCssClass("jspresso-root");
      this.__applicationContainer.add(this.__managerContainer, {flex: 1});
      var manager = new org.jspresso.framework.application.frontend.controller.qx.mobile.EnhancedManager(this.isTablet(),
          this.__managerContainer);
      if (manager.getMasterButton()) {
        manager.setHideMasterButtonCaption(this.translate("Hide"));
      }
      manager.getDetailNavigation().getLayout().setShowAnimation(true);
      this.__blankPage = new qx.ui.mobile.page.NavigationPage();
      manager.addDetail(this.__blankPage);
      this.__blankPage.show();
      return manager;
    },

    _createViewFactory: function () {
      return new org.jspresso.framework.view.qx.mobile.MobileQxViewFactory(this, this, this);
    },

    /**
     * @param initCommand {org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand}
     * @return {undefined}
     */
    _handleInitCommand: function (initCommand) {
      this.base(arguments, initCommand);
      this._popupBookmarkHint();
    },

    /**
     * @param loginCommand {org.jspresso.framework.application.frontend.command.remote.RemoteInitLoginCommand}
     */
    _handleInitLoginCommmand: function (loginCommand) {
      this.base(arguments, loginCommand);
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
      } else if (command
          instanceof org.jspresso.framework.application.frontend.command.remote.mobile.RemoteAnimationCommand) {
        c = /** @type {org.jspresso.framework.application.frontend.command.remote.mobile.RemoteAnimationCommand} */
            command;
        this._handleAnimationCommand(c);
      } else {
        this.base(arguments, command)
      }
    },

    _showBusy: function (busy) {
      if (busy) {
        this.__busy = true;
        qx.event.Timer.once(function () {
          if (this.__busy) {
            this.__busyPopup.show();
          }
        }, this, 500);
      } else {
        this.__busy = false;
        this.__busyPopup.hide();
      }
    },

    /**
     * @return {undefined}
     */
    _restart: function () {
      if (this._getManager().getMasterContainer()) {
        this._getManager().getMasterContainer().hide();
      }
      // cleanup
      this.__workspacePages = {};
      this.__displayedWorkspaceName = null;
      this.__workspacesMasterPage = null;
      this.__routing.dispose();
      this.__applicationContainer.removeAll();

      // re init
      this.__manager = this.__createManager();
      this.__routing = this.__createRouting();
      this.base(arguments);
    },

    /**
     * @return {qx.ui.mobile.page.NavigationPage}
     */
    getCurrentPage: function () {
      return qx.ui.mobile.page.Page._currentPage;
    },

    /**
     *
     * @param title {String}
     * @param message {String}
     * @param remoteDialogView {org.jspresso.framework.gui.remote.RComponent}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     * @param actions {org.jspresso.framework.gui.remote.RAction[] | qx.ui.mobile.form.Button[]}
     * @param useCurrent {Boolean}
     * @param dimension {org.jspresso.framework.util.gui.Dimension}
     * @param secondaryActionLists {org.jspresso.framework.gui.remote.RActionList[]}
     * @return {undefined}
     */
    _popupDialog: function (title, message, remoteDialogView, icon, actions, useCurrent, dimension,
                            secondaryActionLists) {
      useCurrent = (typeof useCurrent == 'undefined') ? false : useCurrent;

      var dialogView = remoteDialogView;
      if (remoteDialogView instanceof org.jspresso.framework.gui.remote.RComponent) {
        dialogView = this._getViewFactory().createComponent(remoteDialogView);
      }

      var dialogContent = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
      dialogContent.addCssClass("group");

      if (secondaryActionLists) {
        for (var i = 0; i < secondaryActionLists.length; i++) {
          var actionList = secondaryActionLists[i];
          if (actionList.getDescription()) {
            var actionListMessage = new qx.ui.mobile.embed.Html(actionList.getDescription());
            actionListMessage.addCssClass("form-row-group-title");
            dialogContent.add(actionListMessage);
          }
          for (var j = 0; j < actionList.getActions().length; j++) {
            var action = actionList.getActions()[j];
            var actionAsList = new qx.ui.mobile.list.List({
              configureItem: function (item, data, row) {
                item.setTitle(data.getName());
                item.setSubtitle(data.getDescription());
                if (data.getIcon()) {
                  item.setImage(data.getIcon().getImageUrlSpec());
                }
                item.setShowArrow(true);
              }
            });
            actionAsList.addListener("tap", function (evt) {
              var modelAction = evt.getCurrentTarget().getModel().getItem(0);
              this.execute(modelAction);
            }, this);
            actionAsList.addCssClass("jspresso-list");
            actionAsList.setModel(new qx.data.Array([action]));
            dialogContent.add(actionAsList);
          }
        }
      }

      var dialogMessage = new qx.ui.mobile.embed.Html(message);
      dialogMessage.addCssClass("form-row-group-title");
      dialogContent.add(dialogMessage);
      dialogContent.add(dialogView);

      var buttonBox = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());
      buttonBox.getLayout().setAlignX("center");
      buttonBox.addCssClass("group");
      if (actions.length > 0) {
        if (actions[0] instanceof org.jspresso.framework.gui.remote.RAction) {
          var toolBar = this._getViewFactory().createToolBarFromActions(actions, 4, null);
          if (this.isTablet()) {
            toolBar.addCssClass("jspresso-tablet-dialog-actions");
          } else {
            toolBar.addCssClass("jspresso-phone-dialog-actions");
          }
          buttonBox.add(toolBar, {flex: 1});
        } else {
          for (var i = 0; i < actions.length; i++) {
            if (this.isTablet()) {
              actions[i].addCssClass("jspresso-tablet-dialog-actions");
            } else {
              actions[i].addCssClass("jspresso-phone-dialog-actions");
            }
            buttonBox.add(actions[i], {flex: 1});
          }
        }
      }

      var dialogPage = null;
      if (useCurrent && this._dialogStack.length > 1) {
        /** @type {qx.ui.mobile.page.NavigationPage} */
        var topDialogPage = this._dialogStack.pop()[0];
        if (topDialogPage) {
          dialogPage = topDialogPage;
        }
      }
      if (dialogPage == null) {
        dialogPage = new org.jspresso.framework.view.qx.mobile.StandaloneNavigationPage();
        dialogPage.addListener("initialize", function (e) {
          dialogPage.getContent().add(dialogContent, {flex: 1});
          dialogPage.add(buttonBox);
        }, this);
        this._dialogStack.push([dialogPage, null, null]);
        this.__applicationContainer.add(dialogPage);
      } else {
        dialogPage.getContent().removeAll();
        dialogPage.getContent().add(dialogContent, {flex: 1});
        dialogPage.add(buttonBox);
      }
      dialogPage.setTitle(title);
      if (remoteDialogView instanceof org.jspresso.framework.gui.remote.mobile.RMobilePageAware) {
        this._getViewFactory().installPageActions(remoteDialogView, dialogPage);
      }
      if (this.__managerContainer.isVisible()) {
        this.__savedCurrentPage = this.getCurrentPage();
      }

      var masterContainer = this._getManager().getMasterContainer();
      if (this.isTablet() && masterContainer.isVisible()) {
        this.__restoreMasterOnClose = true;
        masterContainer.addListenerOnce("changeVisibility", function () {
          dialogPage.show({animation: "slideup"});
        }, this);
        this._getManager()._onHideMasterButtonTap();
      } else {
        dialogPage.show({animation: "slideup"});
      }
    },

    /**
     * Close top most dialog.
     */
    _closeDialog: function () {
      /** @type {qx.ui.mobile.page.NavigationPage} */
      var pageToDestroy = null;
      if (this._dialogStack && this._dialogStack.length > 1) {
        pageToDestroy = this._dialogStack.pop()[0];
      }
      if (this._dialogStack) {
        var pageToRestore;
        if (this._dialogStack.length == 1) {
          pageToRestore = this.__managerContainer;
        } else {
          pageToRestore = this._dialogStack[this._dialogStack.length - 1][0];
        }
        if (pageToDestroy) {
          pageToDestroy.addListenerOnce("disappear", function () {
            qx.event.Timer.once(function () {
              this.__applicationContainer.remove(pageToDestroy);
              pageToDestroy.destroy();
            }, this, 500);
          }, this);
        }
        pageToRestore.show({animation: "slideup", reverse: true});
        if (this.__savedCurrentPage) {
          qx.ui.mobile.page.Page._currentPage = this.__savedCurrentPage;
          this.__savedCurrentPage = null;
        }
        if (this.isTablet() && this.__restoreMasterOnClose && pageToRestore == this.__managerContainer) {
          this._getManager()._onMasterButtonTap();
        }
      }
    },

    /**
     * @param initCommand {org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand}
     * @return {undefined}
     */
    _initApplicationFrame: function (initCommand) {

      var applicationDescription = initCommand.getApplicationDescription();
      var workspaceNames = initCommand.getWorkspaceNames();
      var workspaceDescriptions = initCommand.getWorkspaceDescriptions();
      var workspaceActions = initCommand.getWorkspaceActions();
      var exitAction = initCommand.getExitAction();
      var navigationActions = initCommand.getNavigationActions();
      var actions = initCommand.getActions();
      var secondaryActions = initCommand.getSecondaryActions();
      var helpActions = initCommand.getHelpActions();
      var size = initCommand.getSize();

      this.__workspacesMasterPage = new qx.ui.mobile.page.NavigationPage();
      this.__exitAction = exitAction;
      var workspaceItemListDelegate = {
        configureItem: function (item, data, row) {
          item.setTitle(data.workspaceAction.getName());
          item.setSubtitle(data.workspaceAction.getDescription());
          if (data.workspaceAction.getIcon()) {
            item.setImage(data.workspaceAction.getIcon().getImageUrlSpec());
          }
          item.setShowArrow(true);
        }
      };
      this.__workspacesMasterPage.addListener("initialize", function (e) {
        var pageContent = this.__workspacesMasterPage.getContent();
        if (applicationDescription) {
          var descriptionLabel = new qx.ui.mobile.basic.Label(applicationDescription);
          descriptionLabel.addCssClass("group");
          pageContent.add(descriptionLabel);
        }
        for (var i = 0; i < workspaceActions.getActions().length; i++) {
          var workspaceModel = new qx.data.Array([
            {
              workspaceName: workspaceNames[i],
              workspaceAction: workspaceActions.getActions()[i]
            }
          ]);
          var workspaceNavigator = new qx.ui.mobile.list.List(workspaceItemListDelegate);
          workspaceNavigator.addCssClass("jspresso-list");
          workspaceNavigator.addCssClass("group");
          workspaceNavigator.setModel(workspaceModel);
          workspaceNavigator.addListener("tap", function (evt) {
            var model = evt.getCurrentTarget().getModel().getItem(0);
            var workspaceAction = model.workspaceAction;
            this.__routing.executeGet("/workspace/" + model.workspaceName, {animation: "cube", reverse: false});
            this.execute(workspaceAction);
          }, this);
          if (workspaceDescriptions[i]) {
            var workspaceDescription = new qx.ui.mobile.basic.Label(workspaceDescriptions[i]);
            workspaceDescription.addCssClass("group");
            pageContent.add(workspaceDescription);
          }
          pageContent.add(workspaceNavigator);
        }
      }, this);
      this.bind("name", this.__workspacesMasterPage, "title");
      this._getManager().addMaster(this.__workspacesMasterPage);
      if (this.isTablet()) {
        this._getViewFactory().installPageMainAction(this.__blankPage, exitAction);
      } else {
        this._getViewFactory().installPageMainAction(this.__workspacesMasterPage, exitAction);
      }
      this.__routing.executeGet("/workspaces", {animation: "cube", reverse: false});

      qx.event.Timer.once(function () {
        if (this.isTablet() && (this.getCurrentPage() == null || this.getCurrentPage() == this.__workspacesMasterPage
            || this.getCurrentPage() == this.__blankPage)) {
          this._getManager().getMasterContainer().show();
        }
      }, this, 1000);
    },

    /**
     * @param workspaceName {String}
     * @param workspaceView {org.jspresso.framework.gui.remote.RComponent}
     * @return {undefined}
     */
    _displayWorkspace: function (workspaceName, workspaceView) {
      if (workspaceName == this.__displayedWorkspaceName) {
        return;
      }
      this.__displayedWorkspaceName = workspaceName;
      if (workspaceView) {
        /** @type {qx.ui.mobile.page.NavigationPage} */
        var workspacePage = this.createComponent(workspaceView);
        this.__workspacePages[workspaceName] = workspacePage;
        if (this.isTablet()) {
          this._getViewFactory().installPageMainAction(workspacePage, this.__exitAction);
        } else {
          this._getViewFactory().linkNextPageBackButton(workspacePage, this.__workspacesMasterPage,
              workspaceView.getBackAction(), "cube");
        }
      }
      this.__routing.executeGet("/workspace/" + workspaceName, {animation: "cube", reverse: false});
    },

    /**
     * @param backCommand {org.jspresso.framework.application.frontend.command.remote.mobile.RemoteBackCommand}
     */
    _handleBackCommand: function (backCommand) {
      var wasEnabled = this._changeNotificationsEnabled;
      try {
        this._changeNotificationsEnabled = true;
        this.getCurrentPage().back();
      } finally {
        this._changeNotificationsEnabled = wasEnabled;
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
          this._getApplication().getRoot().remove(messageDialog);
          messageDialog.hide();
          messageDialog.destroy();
        }, this);
        buttonBox.add(okButton);
      }
      this.__showDialog(messageDialog);
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
     * @param page {qx.ui.mobile.page.Page}
     * @return {undefined}
     * @param animation {string}
     * @param back {boolean}
     */
    showDetailPage: function (page, animation, back) {
      if (typeof animation === undefined) {
        animation = "slide";
      }
      if (typeof back === undefined) {
        back = false;
      }
      var pageGuid = page.getUserData("pageGuid");
      if (pageGuid && !back) {
        this.__routing.executeGet("/page/" + pageGuid, {animation: animation, back: back});
      } else {
        this.showPage(page, animation, back);
      }
    },

    /**
     * @param navigationPage {qx.ui.mobile.page.NavigationPage}
     * @return {undefined}
     */
    addDetailPage: function (navigationPage) {
      this._getManager().addDetail(navigationPage);
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
      if (component instanceof qx.ui.mobile.page.NavigationPage) {
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
      if (component instanceof qx.ui.mobile.page.NavigationPage) {
        this._getViewFactory().edit(/** qx.ui.mobile.page.NavigationPage */ component);
      }
    },

    /**
     * @param abstractDialogCommand {org.jspresso.framework.application.frontend.command.remote.RemoteAbstractDialogCommand}
     * @return {undefined}
     */
    _handleDialogCommand: function (abstractDialogCommand) {
      var dialogView;
      var icon;
      if (abstractDialogCommand
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteDialogCommand) {
        var dialogCommand = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteDialogCommand} */
            abstractDialogCommand;
        dialogView = dialogCommand.getView();
        icon = dialogCommand.getView().getIcon();
      }
      this._popupDialog(abstractDialogCommand.getTitle(), null, dialogView, icon, abstractDialogCommand.getActions(),
          abstractDialogCommand.getUseCurrent(), abstractDialogCommand.getDimension());
    },

    /**
     * @param remoteOpenUrlCommand {org.jspresso.framework.application.frontend.command.remote.RemoteOpenUrlCommand}
     * @return {undefined}
     */
    _handleOpenUrlCommand: function (remoteOpenUrlCommand) {
      var popup = new qx.ui.mobile.dialog.Popup();
      popup.setTitle("Download");
      var downloadButton = new qx.ui.mobile.form.Button("Download");
      downloadButton.addListener("tap", function (e) {
        window.open(remoteOpenUrlCommand.getUrlSpec(), remoteOpenUrlCommand.getTarget());
        popup.destroy();
      }, this);
      popup.add(downloadButton);
      popup.show();
    },

    /**
     * @param localeCommand {org.jspresso.framework.application.frontend.command.remote.RemoteLocaleCommand}
     * @return {undefined}
     */
    _handleRemoteLocaleCommand: function (localeCommand) {
      this.base(arguments, localeCommand);
      if (this._getManager().getMasterButton()) {
        this._getManager().setHideMasterButtonCaption(this.translate("Hide"));
      }
      this.__busyIndicator.setLabel(this.translate("Wait") + "...");
      this.__busyPopup.setTitle(this.translate("Loading") + "...");
    },

    /**
     * @return {Array}
     */
    _getKeysToTranslate: function () {
      /** @type {Array} */
      var keysToTranslate = this.base(arguments);
      keysToTranslate = keysToTranslate.concat([
        "m_01", "m_02", "m_03", "m_04", "m_05", "m_06", "m_07", "m_08", "m_09", "m_10", "m_11", "m_12", "Hide", "Wait",
        "Loading", "Clear", "Choose", "Replace"]);
      var bookmarkHintKey = this._determineBrowserBookmarkHintKey();
      if (bookmarkHintKey) {
        keysToTranslate = keysToTranslate.concat([bookmarkHintKey]);
      }
      return keysToTranslate;
    },

    /**
     * @return {String}
     */
    _getClientType: function () {
      if (this.isTablet()) {
        return "MOBILE_HTML5_TABLET";
      } else {
        return "MOBILE_HTML5_PHONE";
      }
    },

    /**
     * @param historyDisplayCommand {org.jspresso.framework.application.frontend.command.remote.RemoteHistoryDisplayCommand}
     * @return {undefined}
     */
    _handleHistoryDisplayCommand: function (historyDisplayCommand) {
      // Not supported in mobile environment
    },

    /**
     * @param animationCommand {org.jspresso.framework.application.frontend.command.remote.mobile.RemotAnimationCommand}
     * @return {undefined}
     */
    _handleAnimationCommand: function (animationCommand) {
      var page = this.getCurrentPage();
      if (animationCommand.getTargetPeerGuid()) {
        page = this.getRegistered(animationCommand.getTargetPeerGuid());
      }
      page.show();
      var cardAnimation = new qx.ui.mobile.layout.CardAnimation();
      var pageElement = page.getContentElement();
      var animation = qx.bom.element.Animation.animate(pageElement,
          cardAnimation.getAnimation(animationCommand.getAnimation(), animationCommand.getDirection(),
              animationCommand.getReverse()), animationCommand.getDuration());
      animation.addListenerOnce("end", function (e) {
        if (animationCommand.getHideView()) {
          page.exclude();
        }
        if (animationCommand.getCallbackAction()) {
          this.execute(animationCommand.getCallbackAction());
        }
      }, this);
    },

    _determineBrowserBookmarkHintKey: function () {
      var deviceType = qx.core.Environment.get("device.type");
      var osName = qx.core.Environment.get("os.name");
      var osVersion = qx.core.Environment.get("os.version");
      var browserName = qx.core.Environment.get("browser.name");
      var browserVersion = qx.core.Environment.get("browser.version");

      var bookmarkHintKey = null;
      if (deviceType == "mobile" || deviceType == "tablet") {
        if (osName.indexOf("ios") >= 0) {
          if (osVersion >= "7") {
            bookmarkHintKey = "bookmark_ios7orlater";
          } else if (osVersion >= "4.2") {
            bookmarkHintKey = "bookmark_ios42orlater";
          } else {
            bookmarkHintKey = "bookmark_ioslegacy";
          }
        } else if (osName.indexOf("android") >= 0) {
          if (osVersion >= "4.4") {
            bookmarkHintKey = "bookmark_android44";
          } else if (osVersion >= "4.1") {
            bookmarkHintKey = "bookmark_android41";
          } else if (osVersion >= "4") {
            bookmarkHintKey = "bookmark_android4";
          } else if (osVersion >= "3") {
            bookmarkHintKey = "bookmark_android3";
          } else {
            bookmarkHintKey = "bookmark_android";
          }
        } else if (osName.indexOf("blackberry") >= 0) {
          bookmarkHintKey = "bookmark_blackberry";
        } else if (osName.indexOf("playbook") >= 0) {
          bookmarkHintKey = "bookmark_playbook";
        }
      }
      //return "bookmark_ios7orlater";
      return bookmarkHintKey;
    },

    _popupBookmarkHint: function () {
      var bookmarkHintKey = this._determineBrowserBookmarkHintKey();
      if (bookmarkHintKey) {
        var content = new qx.ui.mobile.basic.Label(this.translate(bookmarkHintKey));
        var drawer = this._popupDrawer("bottom", content);
        qx.event.Timer.once(function () {
          drawer.hide();
        }, this, 5000);
      }
    },

    _popupDrawer: function (orientation, content) {
      var drawer = this.__drawers[orientation];
      if (!drawer) {
        drawer = new qx.ui.mobile.container.Drawer();
        drawer.setOrientation(orientation);
        drawer.setTapOffset(0);
        drawer.setPositionZ("above");
        if (orientation == "bottom" || orientation == "top") {
          drawer.setLayout(new qx.ui.mobile.layout.VBox());
          drawer._setStyle("height", "initial");
        } else {
          drawer.setLayout(new qx.ui.mobile.layout.HBox());
          drawer._setStyle("width", "initial");
        }
      }
      drawer.removeAll();
      drawer.add(content);
      drawer.show();
      return drawer;
    }
  }
});
