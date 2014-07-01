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
qx.Class.define("org.jspresso.framework.application.frontend.controller.qx.DefaultQxController", {

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
    this._getApplication().getRoot().set({
      blockerColor: '#bfbfbf',
      blockerOpacity: 0.5
    });
  },

  members: {

    /** @type {qx.ui.form.RadioGroup} */
    __workspaceAccordionGroup: null,
    /** @type {qx.ui.container.Stack} */
    __workspaceStack: null,
    /** @type {qx.ui.embed.Iframe} */
    __dlFrame: null,
    /** @type {qx.ui.basic.Label} */
    __statusBar: null,


    _createViewFactory: function () {
      return new org.jspresso.framework.view.qx.DefaultQxViewFactory(this, this, this);
    },

    _showBusy: function (busy) {
      if (busy) {
        this._getApplication().getRoot().setGlobalCursor("wait");
      } else {
        this._getApplication().getRoot().setGlobalCursor("default");
      }
    },

    /**
     *
     * @param title {String}
     * @param message {String}
     * @param remoteDialogView {org.jspresso.framework.gui.remote.RComponent}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     * @param actions {org.jspresso.framework.gui.remote.RAction[] | qx.ui.form.Button[]}
     * @param useCurrent {Boolean}
     * @param dimension {org.jspresso.framework.util.gui.Dimension}
     * @param secondaryActionLists {org.jspresso.framework.gui.remote.RActionList[]}
     * @return {undefined}
     */
    _popupDialog: function (title, message, remoteDialogView, icon, actions, useCurrent, dimension, secondaryActionLists) {
      useCurrent = (typeof useCurrent == 'undefined') ? false : useCurrent;

      var dialogView = remoteDialogView;
      if (remoteDialogView instanceof org.jspresso.framework.gui.remote.RComponent) {
        dialogView = this._getViewFactory().createComponent(remoteDialogView);
      }
      var buttonBox = new qx.ui.container.Composite();
      buttonBox.setLayout(new qx.ui.layout.HBox(10, "right"));

      var dialogBox = new qx.ui.container.Composite();
      dialogBox.setLayout(new qx.ui.layout.VBox(10, null, new qx.ui.decoration.Decorator().set({
        width: 1
      })));

      if (message) {
        var messageLabel = new qx.ui.basic.Label(message);
        messageLabel.setRich(org.jspresso.framework.util.html.HtmlUtil.isHtml(message));
        dialogBox.add(messageLabel);
      }
      if (dimension != null) {
        dialogView.setWidth(dimension.getWidth());
        dialogView.setHeight(dimension.getHeight());
      }
      dialogBox.add(dialogView, {flex: 1});
      var allActions;
      if (secondaryActionLists) {
        allActions = actions.concat(this._getViewFactory().extractAllActions(secondaryActionLists));
      } else {
        allActions = actions;
      }
      for (var i = 0; i < allActions.length; i++) {
        if (allActions[i] instanceof org.jspresso.framework.gui.remote.RAction) {
          buttonBox.add(this._getViewFactory().createAction(allActions[i]));
        } else {
          buttonBox.add(allActions[i]);
        }
      }
      dialogBox.add(buttonBox);

      /**
       * @type {qx.ui.window.Window}
       */
      var dialog;
      var newDialog = true;
      if (useCurrent && this._dialogStack && this._dialogStack.length > 1) {
        dialog = this._dialogStack[this._dialogStack.length - 1][0];
        dialog.removeAll();
        newDialog = false;
      } else {
        var dialogParent;
        //        if(this._dialogStack && this._dialogStack.length > 1) {
        //          dialogParent = this._dialogStack[_dialogStack.length -1];
        //        } else {
        //          dialogParent = this._getApplication().getRoot();
        //        }
        dialogParent = this._getApplication().getRoot();
        dialog = new qx.ui.window.Window();
        dialog.setLayout(new qx.ui.layout.Grow());
        dialog.set({
          modal: true,
          showClose: false,
          showMaximize: false,
          showMinimize: false
        });
        dialogParent.add(dialog);
        this._dialogStack.push([dialog, null, null]);
      }
      dialog.setCaption(title);
      this._getViewFactory().setIcon(dialog, icon);
      if (actions.length > 0) {
        dialog.addListener("keypress", function (e) {
          if (e.getKeyIdentifier() == "Enter" && !qx.ui.core.FocusHandler.getInstance().isFocused(actions[0])
              && (!(qx.ui.core.FocusHandler.getInstance().getFocusedWidget() instanceof qx.ui.form.AbstractField)
                  || qx.ui.core.FocusHandler.getInstance().getFocusedWidget() instanceof qx.ui.form.PasswordField)) {
            actions[0].focus();
            new qx.util.DeferredCall(function () {
              actions[0].execute(); // and call the default button's
            }).schedule();
          }
        });
      }

      if (this.__workspaceStack && this.__workspaceStack.getBounds()) {
        dialog.setMaxWidth(Math.floor(this.__workspaceStack.getBounds().width * 90 / 100));
        dialog.setMaxHeight(Math.floor(this.__workspaceStack.getBounds().height * 90 / 100));
      }
      dialog.add(dialogBox);
      dialog.open();
      if (newDialog) {
        dialog.center();
      }
      this._getViewFactory().focus(dialogBox);
    },

    /**
     * Close top most dialog.
     */
    _closeDialog: function () {
      if (this._dialogStack && this._dialogStack.length > 1) {
        /** @type {qx.ui.window.Window} */
        var topDialog = this._dialogStack.pop()[0];
        this._getApplication().getRoot().remove(topDialog);
        topDialog.close();
        topDialog.destroy();
      }
    },

    /**
     *
     * @param applicationFrame {qx.ui.container.Composite}
     * @param exitAction {org.jspresso.framework.gui.remote.RAction}
     * @param navigationActions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param actions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param helpActions {org.jspresso.framework.gui.remote.RActionList[]}
     * @return {undefined}
     *
     */
    _decorateApplicationFrame: function (applicationFrame, exitAction, navigationActions, actions, helpActions) {
      //var menuBar = this._createApplicationMenuBar(workspaceActions, actions, helpActions);
      //applicationFrame.add(menuBar);
      var toolBar = new qx.ui.toolbar.ToolBar();
      this._getViewFactory().installActionLists(toolBar, navigationActions);
      if (actions) {
        for (var i = 0; i < actions.length; i++) {
          var splitButton = this._getViewFactory().createSplitButton(actions[i]);
          if (splitButton) {
            toolBar.add(splitButton);
          }
        }
      }
      //this._getViewFactory().installActionLists(toolBar, actions);
      toolBar.addSpacer();
      toolBar.add(this._getStatusBar());
      //toolBar.addSpacer();
      if (helpActions) {
        for (var i = 0; i < helpActions.length; i++) {
          var splitButton = this._getViewFactory().createSplitButton(helpActions[i]);
          if (splitButton) {
            toolBar.add(splitButton);
          }
        }
      }
      //this._getViewFactory().installActionLists(toolBar, helpActions);
      toolBar.add(this._getViewFactory().createAction(exitAction));
      applicationFrame.add(toolBar);
    },

    /**
     * @param initCommand {org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand}
     * @return {undefined}
     */
    _initApplicationFrame: function (initCommand) {

      var workspaceNames = initCommand.getWorkspaceNames();
      var workspaceActions = initCommand.getWorkspaceActions();
      var exitAction = initCommand.getExitAction();
      var navigationActions = initCommand.getNavigationActions();
      var actions = initCommand.getActions();
      var secondaryActions = initCommand.getSecondaryActions();
      var helpActions = initCommand.getHelpActions();
      var size = initCommand.getSize();

      var applicationFrame = new qx.ui.container.Composite(new qx.ui.layout.VBox());

      this.__statusBar = new qx.ui.basic.Label();
      this.__statusBar.setVisibility("excluded");
      this._decorateApplicationFrame(applicationFrame, exitAction, navigationActions, actions, helpActions);

      var workspaceAccordion = new qx.ui.container.Composite(new qx.ui.layout.VBox(5));
      this.__workspaceAccordionGroup = new qx.ui.form.RadioGroup();
      this.__workspaceAccordionGroup.setAllowEmptySelection(false);
      for (var i = 0; i < workspaceActions.getActions().length; i++) {
        var workspacePanel = new collapsablepanel.Panel(workspaceActions.getActions()[i].getName());
        if (i == 0) {
          workspacePanel.setValue(true);
        } else {
          workspacePanel.setValue(false);
        }
        workspacePanel.setUserData("workspaceName", workspaceNames[i]);
        workspacePanel.setGroup(this.__workspaceAccordionGroup);
        workspacePanel.setUserData("rAction", workspaceActions.getActions()[i]);
        workspacePanel.addListener("changeValue", function (event) {
          this.execute(event.getTarget().getUserData("rAction"));
        }, this);
        this._getViewFactory().setIcon(workspacePanel.getChildControl("bar"),
            workspaceActions.getActions()[i].getIcon());
        workspaceAccordion.add(workspacePanel, {flex: 1});
      }

      this.__workspaceStack = new qx.ui.container.Stack();

      var splitContainer = new qx.ui.splitpane.Pane("horizontal");
      splitContainer.add(workspaceAccordion, 0.15);
      splitContainer.add(this.__workspaceStack, 0.85);

      applicationFrame.add(splitContainer, {flex: 1});
      if (secondaryActions && secondaryActions.length > 0) {
        var secondaryToolBar = new qx.ui.container.SlideBar();
        secondaryToolBar.add(this._getViewFactory().createToolBarFromActionLists(secondaryActions));
        applicationFrame.add(secondaryToolBar);
      }
      if (size) {
        if (size.getWidth() > 0) {
          applicationFrame.setMinWidth(size.getWidth());
        }
        if (size.getHeight() > 0) {
          applicationFrame.setMinHeight(size.getHeight());
        }
      }
      var scrollContainer = new qx.ui.container.Scroll();
      scrollContainer.add(applicationFrame);

      this._getApplication().getRoot().add(scrollContainer, {edge: 0})
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
        var workspaceViewUI = this.createComponent(workspaceView);
        workspaceViewUI.setUserData("workspaceName", workspaceName);
        this.__workspaceStack.add(workspaceViewUI);
        if (workspaceNavigator) {
          var workspaceNavigatorUI = this.createComponent(workspaceNavigator);
          if (workspaceNavigatorUI instanceof qx.ui.tree.Tree) {
            workspaceNavigatorUI.setHideRoot(true);
          }
          var existingChildren = this.__workspaceAccordionGroup.getChildren();
          var existingChild;
          for (var i = 0; i < existingChildren.length; i++) {
            var child = existingChildren[i];
            if (child.getUserData("workspaceName") == workspaceName) {
              existingChild = child;
            }
          }
          if (existingChild) {
            existingChild.add(workspaceNavigatorUI);
          }
        }
      }
      var children = this.__workspaceStack.getChildren();
      var selectedChild;
      for (var i = 0; i < children.length; i++) {
        var child = children[i];
        if (child.getUserData("workspaceName") == workspaceName) {
          selectedChild = child;
        }
      }
      if (selectedChild) {
        this.__workspaceStack.setSelection([selectedChild]);
      }

      children = this.__workspaceAccordionGroup.getChildren();
      selectedChild = null;
      for (var i = 0; i < children.length; i++) {
        var child = children[i];
        if (child.getUserData("workspaceName") == workspaceName) {
          selectedChild = child;
        }
      }
      if (selectedChild) {
        this.__workspaceAccordionGroup.setSelection([selectedChild]);
      }
    },

    /**
     * @param messageCommand {org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand}
     */
    _handleMessageCommand: function (messageCommand) {
      var messageDialog = new qx.ui.window.Window(messageCommand.getTitle());
      messageDialog.set({
        modal: true,
        showClose: false,
        showMaximize: false,
        showMinimize: false
      });
      messageDialog.setLayout(new qx.ui.layout.VBox(10));
      this._getViewFactory().setIcon(messageDialog, messageCommand.getTitleIcon());
      this._getApplication().getRoot().add(messageDialog);

      var message = new qx.ui.basic.Atom(messageCommand.getMessage());
      message.setRich(org.jspresso.framework.util.html.HtmlUtil.isHtml(messageCommand.getMessage()));
      this._getViewFactory().setIcon(message, messageCommand.getMessageIcon());
      messageDialog.add(message);

      var buttonBox = new qx.ui.container.Composite();
      buttonBox.setLayout(new qx.ui.layout.HBox(10, "right"));
      messageDialog.add(buttonBox);

      var mc;
      if (messageCommand instanceof org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCommand) {
        mc = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCommand } */ messageCommand;
        var yesButton = this._getViewFactory().createYesButton();
        this._getViewFactory().addButtonListener(yesButton, function (event) {
          messageDialog.close();
          messageDialog.destroy();
          this.execute(mc.getYesAction());
        }, this);
        buttonBox.add(yesButton);

        var noButton = this._getViewFactory().createNoButton();
        this._getViewFactory().addButtonListener(noButton, function (event) {
          messageDialog.close();
          messageDialog.destroy();
          this.execute(mc.getNoAction());
        }, this);
        buttonBox.add(noButton);

        if (messageCommand
            instanceof org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCancelCommand) {
          mc = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCancelCommand } */ messageCommand;
          var cancelButton = this._getViewFactory().createCancelButton();
          this._getViewFactory().addButtonListener(cancelButton, function (event) {
            messageDialog.close();
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
          messageDialog.close();
          messageDialog.destroy();
          this.execute(mc.getOkAction());
        }, this);
        buttonBox.add(okButton);

        var cancelButton = this._getViewFactory().createCancelButton();
        this._getViewFactory().addButtonListener(cancelButton, function (event) {
          messageDialog.close();
          messageDialog.destroy();
          this.execute(mc.getCancelAction());
        }, this);
        buttonBox.add(cancelButton);
      } else {
        var okButton = this._getViewFactory().createOkButton();
        this._getViewFactory().addButtonListener(okButton, function (event) {
          this._getApplication().getRoot().remove(messageDialog);
          messageDialog.close();
          messageDialog.destroy();
        }, this);
        buttonBox.add(okButton);
      }

      messageDialog.open();
      messageDialog.center();
    },

    /**
     *
     * @return {qx.ui.basic.Label}
     *
     */
    _getStatusBar: function () {
      return this.__statusBar;
    },

    /**
     * @param actions {org.jspresso.framework.gui.remote.RActionList[]}
     * @param helpActions {org.jspresso.framework.gui.remote.RActionList[]}
     * @return {qx.ui.menubar.MenuBar}
     *
     */
    _createApplicationMenuBar: function (actions, helpActions) {
      var menuBar = new qx.ui.menubar.MenuBar();
      this._completeMenuBar(menuBar, actions, false);
      menuBar.addSpacer();
      this._completeMenuBar(menuBar, helpActions, true);
      return menuBar;
    },

    /**
     *
     * @param menuBar {qx.ui.menubar.MenuBar}
     * @param actionLists {org.jspresso.framework.gui.remote.RActionList[]}
     * @param useSeparator {Boolean}
     * @return {undefined}
     */
    _completeMenuBar: function (menuBar, actionLists, useSeparator) {
      if (actionLists) {
        /** @type {qx.ui.menubar.Button } */
        var menubarButton = null;
        for (var i = 0; i < actionLists.length; i++) {
          var actionList = actionLists[i];
          if (!useSeparator || !menubarButton) {
            menubarButton = this._getViewFactory().createMenubarButton(actionList.getName(),
                actionList.getDescription(), actionList.getIcon());
            menuBar.add(menubarButton);
            menubarButton.setMenu(new qx.ui.menu.Menu());
          } else {
            menubarButton.getMenu().addSeparator();
          }
          var menu = menubarButton.getMenu();
          var menuItems = this._getViewFactory().createMenuItems(actionList.getActions());
          if (menuItems) {
            for (var j = 0; j < menuItems.length; j++) {
              menu.add(menuItems[j]);
            }
          }
        }
      }
    },

    /**
     * @return {Array}
     */
    _getKeysToTranslate: function () {
      /** @type {Array} */
      var keysToTranslate = this.base(arguments);
      keysToTranslate = keysToTranslate.concat([
        "change_font_family", "change_font_size", "format_bold", "format_italic", "format_underline",
        "format_strikethrough", "remove_format", "align_left", "align_center", "align_right", "align_justify",
        "indent_more", "indent_less", "insert_ordered_list", "insert_unordered_list", "undo", "redo"]);
      return keysToTranslate;
    },

    /**
     * @return {String}
     */
    _getClientType: function () {
      return "DESKTOP_HTML5";
    },

    /**
     *
     * @param uploadCommand {org.jspresso.framework.application.frontend.command.remote.RemoteFileUploadCommand}
     */
    _handleFileUpload: function (uploadCommand) {
      var uploadDialog = new qx.ui.window.Window("Upload file");
      uploadDialog.set({
        modal: true,
        showClose: false,
        showMaximize: false,
        showMinimize: false
      });
      uploadDialog.setLayout(new qx.ui.layout.VBox(10));
      //this._getViewFactory().setIcon(uploadDialog, messageCommand.getTitleIcon());
      this._getApplication().getRoot().add(uploadDialog);

      var uploadForm = new uploadwidget.UploadForm('uploadForm', uploadCommand.getFileUrl());
      uploadForm.set({
        decorator: "main",
        padding: 8
      });
      uploadForm.setLayout(new qx.ui.layout.VBox(10));

      var uploadField = new uploadwidget.UploadField('uploadFile', 'Select File', 'icon/16/actions/document-save.png');
      uploadForm.add(uploadField);

      uploadDialog.add(uploadForm, {flex: 1});

      var buttonBox = new qx.ui.container.Composite();
      buttonBox.setLayout(new qx.ui.layout.HBox(10, "right"));
      uploadDialog.add(buttonBox);

      uploadForm.addListener("completed", function (e) {
        this._showBusy(false);
        uploadField.setFileName('');
        var document = uploadForm.getIframeDocument();
        var resource = document.firstChild;
        var id = resource.getAttribute("id");
        if (id) {
          var actionEvent = new org.jspresso.framework.gui.remote.RActionEvent();
          actionEvent.setActionCommand(id);
          this.execute(uploadCommand.getSuccessCallbackAction(), actionEvent);
        }
        uploadDialog.close();
        uploadDialog.destroy();
      }, this);


      var okButton = this._getViewFactory().createOkButton();
      this._getViewFactory().addButtonListener(okButton, function (event) {
        this._showBusy(true);
        uploadForm.send();
      }, this);
      buttonBox.add(okButton);

      var cancelButton = this._getViewFactory().createCancelButton();
      this._getViewFactory().addButtonListener(cancelButton, function (event) {
        this._showBusy(false);
        uploadDialog.close();
        uploadDialog.destroy();
        this.execute(uploadCommand.getCancelCallbackAction());
      }, this);
      buttonBox.add(cancelButton);

      uploadDialog.open();
      uploadDialog.center();
    },

    /**
     *
     * @param downloadCommand {org.jspresso.framework.application.frontend.command.remote.RemoteFileDownloadCommand}
     */
    _handleFileDownload: function (downloadCommand) {
      if (!this.__dlFrame) {
        this.__dlFrame = new qx.ui.embed.Iframe("");
        this.__dlFrame.set({
          width: 0,
          height: 0,
          decorator: null //new qx.ui.decoration.Background("transparent")
        });
        this._getApplication().getRoot().add(this.__dlFrame);
      }
      if (this.__dlFrame.getSource() === downloadCommand.getFileUrl()) {
        this.__dlFrame.resetSource();
      }
      this.__dlFrame.setSource(downloadCommand.getFileUrl());
    },

    /**
     * @return {undefined}
     */
    _restart: function () {
      this._getApplication().getRoot().removeAll();
      this.__dlFrame = null;
      this.base(arguments);
    },

    /**
     * @param remoteUpdateStatusCommand {org.jspresso.framework.application.frontend.command.remote.RemoteUpdateStatusCommand}
     * @return {undefined}
     */
    _updateStatusCommand: function (remoteUpdateStatusCommand) {
      var status = remoteUpdateStatusCommand.getStatus();
      if (status != null && status.length > 0) {
        this.__statusBar.setValue(status);
        this.__statusBar.setVisibility("visible");
      } else {
        this.__statusBar.setVisibility("excluded");
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
      } else if (abstractDialogCommand
          instanceof org.jspresso.framework.application.frontend.command.remote.RemoteFlashDisplayCommand) {
        var flashCommand = /** @type {org.jspresso.framework.application.frontend.command.remote.RemoteFlashDisplayCommand} */
            abstractDialogCommand;
        dialogView = new qx.ui.embed.Flash(flashCommand.getSwfUrl());
        var flashVars = {};
        for (var i = 0; i < abstractDialogCommand.getParamNames().length; i++) {
          flashVars[flashCommand.getParamNames()[i]] = flashCommand.getParamValues()[i];
        }
        dialogView.setVariables(flashVars);
      }
      this._popupDialog(abstractDialogCommand.getTitle(), null, dialogView, icon, dialogButtons,
          abstractDialogCommand.getUseCurrent(), abstractDialogCommand.getDimension());
    },

    /**
     *
     * @param clipboardCommand {org.jspresso.framework.application.frontend.command.remote.RemoteClipboardCommand}
     */
    _handleClipboardCommand: function (clipboardCommand) {
      var dataTransfers = [];
      if (clipboardCommand.getPlainContent()) {
        dataTransfers.push("text/unicode");
        dataTransfers.push(clipboardCommand.getPlainContent());
      }
      if (clipboardCommand.getHtmlContent()) {
        dataTransfers.push("text/html");
        dataTransfers.push(clipboardCommand.getHtmlContent());
      }
      org.jspresso.framework.util.browser.ClipboardHelper.copyToSystemClipboard(dataTransfers);
    },

    /**
     * @param remotePeer {org.jspresso.framework.state.remote.RemoteCompositeValueState|org.jspresso.framework.gui.remote.RTabContainer}
     * @param selectionCommand {org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand}
     * @return {undefined}
     */
    _handleSelectionCommand: function (remotePeer, selectionCommand) {
      if (remotePeer instanceof org.jspresso.framework.gui.remote.RTabContainer) {
        remotePeer.setSelectedIndex(selectionCommand.getLeadingIndex());
      } else {
        this.base(arguments, remotePeer, selectionCommand);
      }
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
      this._getViewFactory().focus(targetPeer.retrievePeer());
    },

    /**
     * @param targetPeer {org.jspresso.framework.gui.remote.RComponent}
     * @param editCommand {org.jspresso.framework.application.frontend.command.remote.RemoteEditCommand}
     * @return {undefined}
     */
    _handleEditCommand: function (targetPeer, editCommand) {
      this._getViewFactory().edit(targetPeer.retrievePeer());
    },

    /**
     * @param historyDisplayCommand {org.jspresso.framework.application.frontend.command.remote.RemoteHistoryDisplayCommand}
     * @return {undefined}
     */
    _handleHistoryDisplayCommand: function (historyDisplayCommand) {
      if (historyDisplayCommand.getSnapshotId()) {
        this.__lastReceivedSnapshotId = historyDisplayCommand.getSnapshotId();
        qx.bom.History.getInstance().addToHistory("snapshotId=" + historyDisplayCommand.getSnapshotId(),
            historyDisplayCommand.getName());
      } else if (historyDisplayCommand.getName()) {
        qx.bom.History.getInstance().setTitle(historyDisplayCommand.getName());
      }
    },

    /**
     * @param remoteOpenUrlCommand {org.jspresso.framework.application.frontend.command.remote.RemoteOpenUrlCommand}
     * @return {undefined}
     */
    _handleOpenUrlCommand: function (remoteOpenUrlCommand) {
      window.open(remoteOpenUrlCommand.getUrlSpec(), remoteOpenUrlCommand.getTarget());
    },

    /**
     * @return {undefined}
     */
    __linkBrowserHistory: function () {
      /**
       * @type {qx.bom.History}
       */
      var browserManager = qx.bom.History.getInstance();
      browserManager.addListener("request", function (e) {
        var state = e.getData();
        var vars = state.split('&');
        var decodedFragment = {};
        for (var i = 0; i < vars.length; i++) {
          var tmp = vars[i].split('=');
          decodedFragment[tmp[0]] = tmp[1];
        }
        if (decodedFragment.snapshotId && decodedFragment.snapshotId != this.__lastReceivedSnapshotId) {
          var command = new org.jspresso.framework.application.frontend.command.remote.RemoteHistoryDisplayCommand();
          command.setSnapshotId(decodedFragment.snapshotId);
          this.registerCommand(command);
        }
      }, this);
    },

    /**
     * @param initCommand {org.jspresso.framework.application.frontend.command.remote.RemoteInitCommand}
     * @return {undefined}
     */
    _handleInitCommand: function (initCommand) {
      this.__linkBrowserHistory();
      this.base(arguments, initCommand);
    }

  }
});
