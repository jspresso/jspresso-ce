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
qx.Class.define("org.jspresso.framework.application.frontend.controller.qx.MobileQxController", {

  extend: org.jspresso.framework.application.frontend.controller.qx.AbstractQxController,

  implement: [
  ],

  statics: {
  },

  construct: function (application, remoteController, userLanguage) {
    this.base(arguments, application, remoteController, userLanguage);

    var busyIndicator = new qx.ui.mobile.dialog.BusyIndicator(this.translate("Wait")+"...");
    this.__busyPopup = new qx.ui.mobile.dialog.Popup(busyIndicator);
    this.__busyPopup.setTitle(this.translate("Loading")+"...");

    this._manager = new qx.ui.mobile.page.Manager(true);
    this._manager.getMasterButton().setVisibility("excluded");
    this._manager.setHideMasterButtonCaption(this.translate("Hide"));
  },

  members: {
    /** @type {qx.ui.mobile.dialog.Popup} */
    __busyPopup: null,
    /** @type {qx.ui.mobile.page.Manager} */
    _manager: null,
    /** @type {qx.ui.mobile.container.Composite} */
    __workspacesNavigator: null,

    __workspacesPages: {},


    _createViewFactory: function () {
      return new org.jspresso.framework.view.qx.MobileQxViewFactory(this, this, this);
    },

    _showBusy: function(busy) {
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

      var dialogPage = new qx.ui.mobile.page.NavigationPage();
      dialogPage.setTitle(title);

      var dialogMessage = new qx.ui.mobile.embed.Html(message);
      var group = new qx.ui.mobile.form.Group([dialogMessage, dialogView]);

      dialogPage.addListener("initialize", function (e) {
        var content = dialogPage.getContent();
        content.add(dialogMessage);
        content.add(dialogView);
        for (var i = 0; i < buttons.length; i++) {
          content.add(buttons[i]);
        }
      }, this);

      if (useCurrent && this._dialogStack && this._dialogStack.length > 1) {
        /** @type {qx.ui.mobile.page.NavigationPage} */
        var topDialog = this._dialogStack.pop()[0];
        if(this._manager)
          this._manager.getDetailContainer().remove(topDialog);
        //topDialog.exclude();
        //topDialog.destroy();
      }
      this._dialogStack.push([dialogPage, null, null]);
      this._manager.addDetail(dialogPage);
      dialogPage.show();
      this._viewFactory.focus(dialogPage);
    },

    /**
     * Close top most dialog.
     */
    _closeDialog: function () {
      if (this._dialogStack && this._dialogStack.length > 1) {
        /** @type {qx.ui.mobile.page.NavigationPage} */
        var topDialog = this._dialogStack.pop()[0];
        this._manager.getDetailContainer().remove(topDialog);
        //topDialog.exclude();
        //topDialog.destroy();
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
        var workspaceView = new qx.ui.mobile.container.Collapsible("<html><img src=\""
            + workspaceAction.getIcon().getImageUrlSpec() + "\"/> "
            + workspaceAction.getName()+"</html>");
        workspaceView.setUserData("model", {
          name: workspaceNames[i],
          action: workspaceAction
        });
        if (i == 0) {
          workspaceView.setCollapsed(false);
        } else {
          workspaceView.setCollapsed(true);
        }
        workspaceView.addListener("changeCollapsed", function(evt) {
          var openedWsView = /**@type {qx.ui.mobile.Collapsible}*/ evt.getTarget();
          if(!openedWsView.getCollapsed()) {
            for(var j = 0; j < this.__workspacesNavigator.getChildren().length; j++) {
              if(this.__workspacesNavigator.getChildren()[j] != openedWsView) {
                (/**@type {qx.ui.mobile.container.Collapsible}*/ this.__workspacesNavigator.getChildren()[j]).setCollapsed(true);
              }
            }
            var workspaceAction = openedWsView.getUserData("model")["action"]
            this.execute(workspaceAction);
          }
        }, this);
        this.__workspacesNavigator.add(workspaceView);
      }
      var workspacesNavigationPage = new qx.ui.mobile.page.NavigationPage();
      workspacesNavigationPage.setTitle(this.translate("Workspaces"));
      workspacesNavigationPage.addListener("initialize", function (e) {
        var content = workspacesNavigationPage.getContent();
        content.add(this.__workspacesNavigator, {flex: 1});
      }, this);
      this._manager.addMaster(workspacesNavigationPage);

      this._manager.getMasterButton().setVisibility("visible");
      workspacesNavigationPage.show();
      this._manager._onMasterButtonTap();
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
        var workspaceViewUI = /*this.createComponent(workspaceView)*/ new qx.ui.mobile.form.Label(workspaceName);
        var workspacePage = new qx.ui.mobile.page.NavigationPage();
        workspacePage.addListener("initialize", function (e) {
          var content = workspacePage.getContent();
          content.add(workspaceViewUI, {flex: 1});
        }, this);
        this._manager.addDetail(workspacePage);
        this.__workspacesPages[workspaceName] = workspacePage;
        if (workspaceNavigator) {
          var workspaceNavigatorUI = this.createComponent(workspaceNavigator);
          var existingChildren = this.__workspacesNavigator.getChildren();
          var existingChild = null;
          for (var i = 0; existingChild == null && i < existingChildren.length; i++) {
            var child = existingChildren[i];
            if (child.getUserData("model").name == workspaceName) {
              existingChild = child;
            }
          }
          if (existingChild) {
            existingChild.add(workspaceNavigatorUI);
          }
        }
      }
      var wasHideMasterOnDetailStart = this._manager.getHideMasterOnDetailStart();
      this._manager.setHideMasterOnDetailStart(false);
      this.__workspacesPages[workspaceName].show();
      this._manager.setHideMasterOnDetailStart(wasHideMasterOnDetailStart);
      var children = this.__workspacesNavigator.getChildren();
      for (var i = 0; i < children.length; i++) {
        var child = children[i];
        if (child.getUserData("model").name == workspaceName) {
          child.setCollapsed(false);
        }
      }
    }


  }
});
